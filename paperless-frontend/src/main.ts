import {bootstrapApplication} from '@angular/platform-browser';
import {appConfig} from './app/app.config';
import AppComponent from './app/app';
import * as pdfjsLib from 'pdfjs-dist'

// Worker für PDF.js definieren
pdfjsLib.GlobalWorkerOptions.workerSrc = 'assets/pdf.worker.min.mjs';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
