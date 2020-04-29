import { Component, OnInit } from '@angular/core';
import { Patient } from './patient';
import { PatientDataService } from '../patient-data.service';

@Component({
  selector: 'app-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {

  patients: Patient[];
  currentPatient: Patient = {};
  patientId: number = 0;
  constructor(private patientDataService: PatientDataService) {
  }

  ngOnInit(): void {
  }

  addPatient() {
    this.patientDataService.addPatient(this.currentPatient).then(() => {
      this.getAll();
    });
  }

  getAll() {
    this.patientDataService.getPatients().then((result: Patient[]) => {
      this.patients = result;
    });
  }

  removePatient() {
    this.patientDataService.removePatient(this.patientId).then(() => {
      this.getAll();
    });
  }

}
