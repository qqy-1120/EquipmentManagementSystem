import fetch from '../component/fetch'

export function login(user){
    return fetch({
        url:'/api/login',
        method:'post',
        data:user
    })
}