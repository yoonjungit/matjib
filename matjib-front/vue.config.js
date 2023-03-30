module.exports = {
  devServer : {
    proxy:{
      // matjib란 주소로 시작하면 target에 명시된 주소(백엔드)로 요청
      '/matjib' : {
        target : 'http://localhost:8080',
      }
    }
  }
}
