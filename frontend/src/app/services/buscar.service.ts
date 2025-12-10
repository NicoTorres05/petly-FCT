import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BuscarService {
  private searchTermSubject = new Subject<string>();

  setSearchTerm(term: string) {
    this.searchTermSubject.next(term);
  }

  getSearchTerm(): Observable<string> {
    return this.searchTermSubject.asObservable();
  }
}
