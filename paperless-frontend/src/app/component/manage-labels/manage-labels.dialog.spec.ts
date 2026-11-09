import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageLabelsDialog } from './manage-labels.dialog';

describe('ManageLabelsDialog', () => {
  let component: ManageLabelsDialog;
  let fixture: ComponentFixture<ManageLabelsDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageLabelsDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ManageLabelsDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
