import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import ShowPhoto from "../components/ShowPhoto";
import QueryPhoto from "../components/QueryPhoto";
import PhotoManage from "../components/PhotoManage";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/helloWorld',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/',
      name: 'ShowPhoto',
      component: ShowPhoto
    },
    {
      path: '/queryPhoto',
      name: 'QueryPhoto',
      component: QueryPhoto
    },
    {
      path: '/photoManage',
      name: 'PhotoManage',
      component: PhotoManage
    },
  ]
})
