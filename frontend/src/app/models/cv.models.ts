export interface CvUploadDto {
  cvBase64: string;
  nomFichier: string;
  typeFichier: string;
}

export interface CvResponseDto {
  id?: number;
  nomFichier?: string;
  typeFichier?: string;
  dateUpload?: string;
  hasCv: boolean;
} 