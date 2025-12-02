import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    constructor() { }
    private tokenKey = 'authToken';

    handle(token: string) {
        this.set(token);
        return this.loggedIn();
    }

    set(token: string): void {
        localStorage.setItem(this.tokenKey, token);
    }

    get(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    remove(): void {
        localStorage.removeItem(this.tokenKey);
    }

  isValid(): boolean {
    const token = this.get();
    if (token) {
      const payload = this.payload(token);
      if (payload) {
        const isIssuerValid = payload.iss === 'http://localhost:8080';
        const isNotExpired = payload.exp > Math.floor(Date.now() / 1000);

        if (isIssuerValid && isNotExpired) {
          return true;
        } else {
          this.remove();
        }
      }
    }
    return false;
  }


  payload(token: string) {
        const payload = token.split('.')[1];
        return this.decode(payload);
    }

    decode(payload: any) {
        return JSON.parse(atob(payload));
    }

    loggedIn(): boolean {
        return this.isValid();
    }

  getUserData(): any {
    const token = this.get();
    if (!token) return null;

    const payload = this.payload(token);
    if (!payload) return null;

    return payload;
  }

}
