import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, switchMap, filter, take } from 'rxjs/operators';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

// Variables globales pour gérer le refresh
let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

function addTokenHeader(request: HttpRequest<unknown>, token: string | null): HttpRequest<unknown> {
  if (token) {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  return request;
}

function isAuthenticationRequest(url: string): boolean {
  return url.includes('/login') || 
         url.includes('/alternant') || 
         url.includes('/ecole') || 
         url.includes('/recruteur') ||
         url.includes('/refresh-token');
}

function handle401Error(
  request: HttpRequest<unknown>, 
  next: HttpHandlerFn, 
  authService: AuthService
): Observable<HttpEvent<unknown>> {
  if (!isRefreshing) {
    isRefreshing = true;
    refreshTokenSubject.next(null);

    const refreshToken = authService.getRefreshToken();
    console.log('🔄 Intercepteur: Début du refresh token...', refreshToken ? 'Token présent' : 'Pas de token');
    
    if (refreshToken) {
      return authService.refreshToken({ refreshToken }).pipe(
        switchMap((response) => {
          isRefreshing = false;
          console.log('🔄 Intercepteur: Réponse refresh reçue', response);
          
          if (response.success && response.accessToken && response.refreshToken) {
            console.log('✅ Intercepteur: Refresh réussi, nouveaux tokens sauvegardés');
            authService.saveTokens(response.accessToken, response.refreshToken);
            refreshTokenSubject.next(response.accessToken);
            
            return next(addTokenHeader(request, response.accessToken));
          }
          
          console.log('❌ Intercepteur: Refresh échoué, déconnexion...');
          return logoutUser(authService);
        }),
        catchError((error) => {
          console.log('❌ Intercepteur: Erreur lors du refresh', error);
          isRefreshing = false;
          return logoutUser(authService);
        })
      );
    }
    
    console.log('❌ Intercepteur: Pas de refresh token, déconnexion...');
    return logoutUser(authService);
  }

  // Si un refresh est déjà en cours, attendre qu'il se termine
  console.log('⏳ Intercepteur: Refresh en cours, attente...');
  return refreshTokenSubject.pipe(
    filter(token => token !== null),
    take(1),
    switchMap((token) => next(addTokenHeader(request, token)))
  );
}

function logoutUser(authService: AuthService): Observable<HttpEvent<unknown>> {
  authService.logout();
  // Rediriger vers la page de login
  window.location.href = '/login';
  return throwError(() => new Error('Session expirée'));
}

export const AuthInterceptor: HttpInterceptorFn = (
  request: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {
  
  const authService = inject(AuthService);
  
  // Ne pas ajouter le token pour les requêtes de login et d'inscription
  const isAuthRequest = isAuthenticationRequest(request.url);
  
  if (!isAuthRequest) {
    const token = authService.getAccessToken();
    console.log('🔐 Intercepteur: Token récupéré:', token ? 'présent' : 'absent');
    if (token) {
      console.log('🔐 Intercepteur: Token (premiers caractères):', token.substring(0, 20) + '...');
    }
    request = addTokenHeader(request, token);
    console.log('🔐 Intercepteur: Ajout du token à la requête', request.url);
  }

  return next(request).pipe(
    catchError(error => {
      // Si erreur 401 et que ce n'est pas une requête d'auth, essayer de refresh
      if (error instanceof HttpErrorResponse && error.status === 401 && !isAuthRequest) {
        console.log('❌ Intercepteur: Erreur 401 détectée, tentative de refresh...', request.url);
        return handle401Error(request, next, authService);
      }
      
      return throwError(() => error);
    })
  );
}; 