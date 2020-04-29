import { Injectable } from '@angular/core';
import { Patient } from './patient/patient';
import { HttpClient } from '@angular/common/http';
import { NgAnalyzeModulesHost } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})
export class PatientDataService {

  constructor(private http: HttpClient) { }

  public addPatient(patient: Patient) {
    let promise = new Promise((resolve, reject) => {
      this.http.post("http://medflix-meet.ap-south-1.elasticbeanstalk.com/patients", patient)
        .toPromise()
        .then((data) => {
          console.log(data);
          resolve();
        }, (reason) => {
          console.log("Error Occured : " + reason);
          reject();
        })
    });
    return promise;
  }

  public removePatient(patientId: number) {
    let promise = new Promise((resolve, reject) => {
      this.http.delete("http://medflix-meet.ap-south-1.elasticbeanstalk.com/patient/" + patientId)
        .toPromise()
        .then((data) => {
          console.log(data);
          resolve();
        }, (reason) => {
          console.log("Error Occured : " + reason);
          reject();
        })
    });
    return promise;
  }


  public getPatients() {
    let promise = new Promise((resolve, reject) => {
      this.http.get("http://medflix-meet.ap-south-1.elasticbeanstalk.com/patients")
        .toPromise()
        .then((data: Patient[]) => {
          console.log(data);
          resolve(data);
        }, (reason) => {
          console.log("Error Occured : " + reason);
          reject();
        })
    });
    return promise;
  }
}
