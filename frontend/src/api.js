import jwt_decode from "jwt-decode";

export const BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api/v1";

function hasRole(role) {
  const token = getToken();
  if (token == null) return false;
  try {
    return jwt_decode(token).role === role;
  } catch (err) {
    return false;
  }
}

export function isAdmin() {
  return hasRole("ADMIN");
}

export function isGuest() {
  return hasRole("GUEST");
}

export function getToken() {
  let token = localStorage.getItem("token");
  if (token) {
    token = token.replaceAll('"', "");
  }
  return token;
}
