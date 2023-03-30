import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate";

const store = createStore({
    state () {
        return {
            loginState : 0,
            account : {
                id : 0,
                nickname : 0
            }
        }
    },

    plugins: [createPersistedState()],

    mutations: {
        setAccount(state, payload) {
            state.account.id = payload;
        },
        setNState(state, payload) {
            state.loginState = payload;
        },
        setNickname(state, payload) {
            state.account.nickname = payload;
        }
    }
})

export default store;