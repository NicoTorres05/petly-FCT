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

    isValid() {
        const token = this.get();
        if (token) {
            const payload = this.payload(token);

            /* console.log(payload); */

            if (payload) {
                const isIssuerValid = payload.iss === 'http://localhost:8080';
                const isNotExpired = payload.exp > Math.floor(Date.now() / 1000);

                return isIssuerValid && isNotExpired;
            }
        }
        return false;
    }

    payload(token: string) {
        const payload = token.split('.')[1]; // Pilla el payload por la segunda parte del token (Son 3 partes separadas por un punto)
        return this.decode(payload);
    }

    decode(payload: any) {
        return JSON.parse(atob(payload));
    }

    loggedIn(): boolean {
        return !!this.get(); // Devuelve true si hay un token almacenado
    }

  getUserData(): any {
    const token = this.get();
    if (!token) return null;

    const payload = this.payload(token);
    if (!payload) return null;

    // Devuelve el payload completo (contiene datos como email, roles, etc.)
    return payload;
  }

}
