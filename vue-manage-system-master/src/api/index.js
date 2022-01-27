import Axios from "../response/index";

const baseUrl="http://localhost:8083";
/*登录、登出*/
export const submitFormLogin = data => {
    return Axios.post(baseUrl+"/sso-user/login",data);
};

export const loginOutHeader = data => {
    return Axios.post(baseUrl+"/sso-user/logout",data);
};
