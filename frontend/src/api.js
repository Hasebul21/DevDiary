import jwt_decode from "jwt-decode";

export const BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api/v1";

export function isAdmin() {
  const token = getToken();
  if (token == null) return false;
  try {
    return jwt_decode(token).role === "ADMIN";
  } catch (err) {
    return false;
  }
}

export function getToken() {
  let token = localStorage.getItem("token");
  if (token) {
    token = token.replaceAll('"', "");
  }
  return token;
}
