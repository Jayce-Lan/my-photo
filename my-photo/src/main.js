// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router'
import VueResource from 'vue-resource'

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueResource);
//允许发起post请求
Vue.http.options.emulateJSON = true


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
