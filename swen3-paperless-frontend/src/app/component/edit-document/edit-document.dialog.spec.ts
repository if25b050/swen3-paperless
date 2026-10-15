import {ComponentFixture, TestBed} from '@angular/core/testing';

import {EditDocumentDialog} from './edit-document.dialog';

describe('EditDocumentDialog', () => {
  let component: EditDocumentDialog;
  let fixture: ComponentFixture<EditDocumentDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDocumentDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(EditDocumentDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
