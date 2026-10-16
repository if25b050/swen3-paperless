import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditDocumentDialog} from './edit-document.dialog';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

describe('EditDocumentDialog', () => {
  let component: EditDocumentDialog;
  let fixture: ComponentFixture<EditDocumentDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDocumentDialog],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {name: 'Test.pdf'}},
        {provide: MatDialogRef, useValue: {}}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EditDocumentDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
