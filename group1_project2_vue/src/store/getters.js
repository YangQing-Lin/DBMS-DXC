const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  id: state => state.user.id,
  name: state => state.user.name,
  username: state => state.user.username,
  email: state => state.user.email,
  phone: state => state.user.phone,
  type: state => state.user.type,
  create_time: state => state.user.create_time,
  data: state => state.user.data,
}
export default getters
