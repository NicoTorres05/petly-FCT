import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProfileData } from './edit-profile-data';

describe('EditProfileData', () => {
  let component: EditProfileData;
  let fixture: ComponentFixture<EditProfileData>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditProfileData]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditProfileData);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
