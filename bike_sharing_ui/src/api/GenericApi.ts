export enum HTTP_METHOD{
    POST = "POST",GET = "GET",PUT = "PUT"
}

const createGetParams = (data:{[key:string]:string|number|string[]|number[]}): string =>{
    const keys = Object.keys(data);
    let params = "";
    for(let i = 0; i < keys.length; i++){
        params = `${keys[i]}=${data[keys[i]]}&`;
    }
    return params;
}

const getCall = async (url:string, parameters: string,headers:{}):Promise<Response> => {
    return await fetch(`${url}?${parameters}`,{
        method: HTTP_METHOD.GET,

        headers:headers
    });
}
const postCall = async (url: string, body: string, headers:{}):Promise<Response> => {
    let response = await fetch(url,{
        body: body,
        method: HTTP_METHOD.POST,   
        headers:headers,
        
    });    
    return response;
}
const putCall = async (url:string, body: string,headers:{}):Promise<Response> =>{
    return await fetch(url,{
        body: body,
        method: HTTP_METHOD.PUT,

        headers:headers
    });
}

export const performFetch = async (url: string, data:{}, method:HTTP_METHOD,token="") => {
    let response = null;
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': ''
    }
    if(token){
        headers.Authorization = `Bearer ${token}`;
    }

    try{
        switch (method) {
            case HTTP_METHOD.POST:
                response = await postCall(url,JSON.stringify(data),headers);    
                break;
            case HTTP_METHOD.GET:
                const params = createGetParams(data);
                response = await getCall(url,params,headers);
                break;
            case HTTP_METHOD.PUT:
                response = await putCall(url,JSON.stringify(data),headers);    
                break;
            default:
                break;
        }
    }
    catch(fetchException){
        console.error("expection while fetching",fetchException);
    }
    
    return response;
}