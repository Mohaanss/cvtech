export enum UserRole {
  ALTERNANT = 'ALTERNANT',
  ECOLE = 'ECOLE',
  RECRUTEUR = 'RECRUTEUR'
}

export interface LoginDto {
  email: string;
  motDePasse: string;
}

export interface LoginResponseDto {
  id?: number;
  email?: string;
  role?: UserRole;
  dateCreation?: string;
  message: string;
  success: boolean;
  accessToken?: string;
  refreshToken?: string;
  accessTokenExpiresIn?: number;
}

export interface RefreshTokenRequestDto {
  refreshToken: string;
}

export interface RefreshTokenResponseDto {
  accessToken?: string;
  refreshToken?: string;
  accessTokenExpiresIn?: number;
  success: boolean;
  message: string;
}

export interface CreateAlternantDto {
  email: string;
  motDePasse: string;
  nom: string;
  prenom: string;
  telephone?: string;
  ville?: string;
  lienLinkedin?: string;
  lienPortfolio?: string;
  dateNaissance?: string;
}

export interface CreateEcoleDto {
  email: string;
  motDePasse: string;
  nomEcole: string;
  adresse?: string;
  siteWeb?: string;
}

export interface CreateRecruteurDto {
  email: string;
  motDePasse: string;
  entreprise: string;
  service?: string;
  telephone?: string;
  siteWeb?: string;
}

export interface UtilisateurResponseDto {
  id: number;
  email: string;
  role: UserRole;
  dateCreation: string;
} 