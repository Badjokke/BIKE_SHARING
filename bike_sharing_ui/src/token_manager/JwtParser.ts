import { jwtDecode } from "jwt-decode";
import { HTTP_METHOD, performFetch } from "../api/GenericApi";

export interface jwtObject{
    jti: string,
    sub: string,
    iat: number,
    exp: number,
    expiresIn?:number
}
interface GoogleTokenInfo {
    azp: string;
    aud: string;
    sub: string;
    scope: string;
    exp: string;
    expires_in: string;
    email: string;
    email_verified: string;
    access_type: string;
  }

export const decodeJwt = async (token: string):Promise<jwtObject|null|GoogleTokenInfo> => {
    if(token.startsWith("ya29")){
        return await fetchGoogleTokenInfo(token);
    }
    try{
        const decoded = jwtDecode(token) as jwtObject;
        return decoded;
    }
    catch(parsingException:any){
        console.log(`error while parsing token: ${token}, reason: ${parsingException.message}`);
        return null;
    }

}

const fetchGoogleTokenInfo = async (token: string)=>{
    const params = {access_token:token};
    const response = await performFetch("https://www.googleapis.com/oauth2/v3/tokeninfo",params,HTTP_METHOD.GET);
    if(response == null){
        throw new Error("google api down");
    }
    if(response.ok)
        return await response.json() as GoogleTokenInfo;
    return null;
    
}






