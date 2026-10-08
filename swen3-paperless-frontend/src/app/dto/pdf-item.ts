import {SafeResourceUrl} from '@angular/platform-browser';

export interface PdfItem {
  file: File;
  safeUrl: SafeResourceUrl;
  extractedText: string; // Speichert Text dem PDF
}
