import request from '@/utils/request'

export default {
    updatePassword(token, username, oldPassword, newPassword) {
        return request({
            url: '/user/password',
            method: 'post',
            params: {
                token,
                username,
                oldPassword,
                newPassword
            }
        });
    },
    getUserList(searchModel) {
        return request({
            url: '/user/list',
            method: 'get',
            params: {
                pageNo: searchModel.pageNo,
                pageSize: searchModel.pageSize,
                username: searchModel.username,
                phone: searchModel.phone,
                startTime: searchModel.startTime,
                endTime: searchModel.endTime,
            }
        });
    },
    addUser(user) {
        return request({
            url: '/user',
            method: 'post',
            data: user
        });
    },
    updateUser(token, user) {
        return request({
            url: '/user',
            method: 'put',
            data: user,
            params: { token },
        });
    },
    saveUser(token, user) {
        console.log("save user: ", token, user);
        if (user.id == null && user.id == undefined) {
            return this.addUser(user);
        }
        return this.updateUser(token, user);
    },
    getUserById(id) {
        return request({
            //url: '/user/' + id,
            url: `/user/${id}`,
            method: 'get'
        });
    },
    deleteUserById(id) {
        return request({
            url: `/user/${id}`,
            method: 'delete'
        });
    },
}