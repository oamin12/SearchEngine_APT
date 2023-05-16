import {createStore} from 'redux'
const storeReduser = (state=[],

    
    action) => {
      if(action.type === "add"){
          let i= state.findIndex(o=> o.id === action.id);
          const existingItem = state[i];
          let updatedItem ;
          let newItems ;
          if(i!==-1){
             updatedItem = {
                 ...existingItem ,
                 count: existingItem.count + action.count 
             }
             newItems = [...state]
             newItems[i] = updatedItem;
          }else{
              
              updatedItem = {id: action.id , count : action.count, name: action.name, price : action.price , img:action.img, size:action.size}
              newItems = state.concat(updatedItem);
          }
       
          return(newItems)
      }
      if(action.type === "remove"){
          let newItems = state.filter(I=> I.id !== action.id)
          return(newItems)
      }
     
      if(action.type === "delete"){
          return([]);
      }
      return(state)
  }
  
  function loadState(){
      const state = localStorage.getItem('cart')
      if(state !== null){
          
          return JSON.parse(state);
      }else{
  
          return []
      }
    
  }
  
  const saveState =(state)=>{
      localStorage.setItem('cart', JSON.stringify(state));
  }
  const store = createStore( storeReduser,loadState());
  store.subscribe(()=>{
  saveState(store.getState())
  })
  export default store