// backend url, set REACT_APP_API_URL when the server runs somewhere else
export const BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api/v1";

export function getToken() {
  let token = localStorage.getItem("token");
  if (token) {
    token = token.replaceAll('"', "");
  }
  return token;
}
