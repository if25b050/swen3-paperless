import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogTitle} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-confirm.dialog',
  imports: [
    MatDialogTitle,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  templateUrl: './confirm.dialog.html',
  styleUrl: './confirm.dialog.scss',
})
export class ConfirmDialog {
  public text: string = inject(MAT_DIALOG_DATA);

}
