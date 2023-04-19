<template>
  <div class="mypage">
    <div class="edit">
      <div style="font-size: larger; font-weight: bold">회원정보 변경(닉네임 설정)</div>
      <hr>
      <div>이메일은 변경 불가합니다.<br><br>
        찐맛집에서 변경한 닉네임은 네이버 닉네임과 연동되지 않습니다.<br><br></div>
      <div>
        <div style="font-weight: bold">닉네임</div>
        <div class="nickname">
          <input v-model="inputNickname" @keyup="inputCheck" type="text"
                 @keypress="preventSpace"
                 style="font-size: medium" minlength="3" maxlength="10">
          <span> {{ lengthCheckMessage }} </span>
          <button @click="editNickname()"> 변경하기</button>
        </div>
        <div>{{ checkMessage }}</div>
        <br><br>

      </div>
    </div>
    <div class="delete">
      <div style="font-size: larger; font-weight: bold">회원탈퇴</div>
      <hr>
      <span>탈퇴하시면 모든 정보가 즉시 삭제됩니다.<br>탈퇴하시겠습니까?  </span>
      <button @click="deleteMember()"> 탈퇴하기</button>
      <br><br>
      <div>네이버 로그인 연동은 네이버 사이트에서 직접 해제하셔야 합니다.
        <a href="https://nid.naver.com/internalToken/view/tokenList/pc/ko">
          <div>>>네이버 이력관리 바로가기</div>
        </a></div>
    </div>
  </div>
</template>

<script>


import api from "@/main";
import store from "@/store/store";

export default {
  name: 'MyPage',
  components: {},

  data() {
    let lengthCheckMessage = " ( 0 / 최소 3자, 최대 10자)";
    let inputNickname = '';
    let checkMessage = '';

    return {inputNickname, lengthCheckMessage, checkMessage};
  },

  methods: {
    inputCheck(event) {   //글자수 실시간 확인
      const minLength = event.target.minLength;
      if (this.inputNickname.length < minLength) {
        this.lengthCheckMessage = " ( " + this.inputNickname.length + " / 최소 3자, 최대 10자 )";
      } else {
        this.lengthCheckMessage = " ( " + this.inputNickname.length + " / 최소 3자, 최대 10자 )";
      }
    },

    preventSpace(event) {     //공백 입력 금지
      if (event.charCode === 32) {
        event.preventDefault();
      }
    },

    async editNickname() {   //별명 변경
      if (this.inputNickname.length < 3) {
        alert("최소 3자 이상 입력해주세요")
      } else {
        const res = await api.put('/matjib/member/edit', {nickname: this.inputNickname});
        if (res.status == 200) {
          store.commit('setNickname', res.data);
          alert(res.data + "로 변경성공")
        } else {
          alert("변경에 실패했습니다.");
        }
      }
    },

    async deleteMember() {    //회원탈퇴
      let answer = confirm("정말로 탈퇴하시겠습니까?");
      if (answer === true) {
        const res = await api.delete('/matjib/member/delete');
        if (res.status === 200) {
          alert("탈퇴완료")
          this.$logout()
        } else if(res.status === 401 || res.status ===404) {
          alert("오류가 발생했습니다.")
          this.$logout()
        }else {
          alert("오류가 발생했습니다.");
        }
      }
    }
  }
}
</script>

<style scoped>
* {
  color: #666666;
}

.edit {
  margin: 30px;
  width: 500px;
  height: 300px;
  font-size: small;
}

.delete {
  margin: 30px;
  width: 500px;
  height: 300px;
  font-size: small;
}

button {
  border: none;
  border-radius: 4px;
  background-color: #E74C3C;
  color: white;
  font-weight: 550;
}
</style>
