import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpinnerService {
  private _loadingSubject$ = new BehaviorSubject<boolean>(false);

  public get loading$(): Observable<boolean> {
    return this._loadingSubject$.asObservable();
  }

  public setLoading(value: boolean): void {
    this._loadingSubject$.next(value);
  }
}
