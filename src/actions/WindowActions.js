import * as types from '../constants/ActionTypes'
import axios from 'axios';
import config from '../config';

export function createWindow(windowType, docId = "NEW"){
    return (dispatch) => {
        // this chain is really important,
        // to do not re-render widgets on init
        dispatch(initWindow(windowType,docId))
            .then(response => {
                docId = response.data[0].id
                dispatch(initDataSuccess(nullToEmptyStrings(response.data[0].fields)))}
            ).then(response =>
                dispatch(initLayout(windowType))
            ).then(response =>
                dispatch(initLayoutSuccess(response.data))
            ).then(response => {
                response.layout.tabs.map(item => {
                    dispatch(getData(windowType, docId, item.tabid))
                        .then((res)=> {
                            let tab = {};
                            tab[item.tabid] = res.data;
                            dispatch(addRowData(tab));
                        });
                })
            }).catch(()=>{
                dispatch(noConnection());
            })
    }
}

export function noConnection(status){
    return {
        type: types.NO_CONNECTION,
        status: status
    }
}



export function initWindow(windowType, docId) {
    return (dispatch) => {
        if(docId === "NEW"){
            return dispatch(patchRequest(windowType, docId))
        }else{
            return dispatch(getData(windowType, docId))
        }
    }
}

function nullToEmptyStrings(arr){
    return arr.map(item =>
        (item.value === null) ?
        Object.assign({}, item, { value: "" }) :
        item
    )
}

export function initLayout(windowType){
    return dispatch => axios.get(config.API_URL + '/window/layout?type=' + windowType);
}

export function getData(windowType, id, tabId, rowId) {
    return dispatch => axios.get(
        config.API_URL +
        '/window/data?type=' + windowType +
        '&id=' + id +
        (tabId ? "&tabid=" + tabId : "") +
        (rowId ? "&rowid=" + rowId : "")
    );
}

export function initLayoutSuccess(layout) {
    return {
        type: types.INIT_LAYOUT_SUCCESS,
        layout: layout
    }
}
export function initDataSuccess(data) {
    return {
        type: types.INIT_DATA_SUCCESS,
        data: data
    }
}
export function addRowData(data) {
    return {
        type: types.ADD_ROW_DATA,
        data: data
    }
}
export function updateDataSuccess(item) {
    return {
        type: types.UPDATE_DATA_SUCCESS,
        item: item
    }
}

/*
*  Wrapper for patch request of widget elements
*  when responses should merge store
*/
export function patch(windowType, id = "NEW", tabId, rowId, property, value) {
    return dispatch => {
        dispatch(patchRequest(windowType, id, tabId, rowId, property, value)).then(response => {
            response.data[0].fields.map(item => {
                dispatch(updateDataSuccess(item));
            })
        })
    }
}

export function patchRequest(windowType, id = "NEW", tabId, rowId, property, value) {
    let payload = {};

    if(id === "NEW"){
        payload = [];
    }else{
        if(property && value){
            payload = [{
                'op': 'replace',
                'path': property,
                'value': value
            }];
        }else {
            payload = [];
        }
    }


    return dispatch => axios.patch(
            config.API_URL +
            '/window/commit?type=' +
            windowType +
            '&id=' + id +
            (tabId ? "&tabid=" + tabId : "") +
            (rowId ? "&rowId=" + rowId : "")
        , payload);
}

export function updateDataProperty(property, value){
    return {
        type: types.UPDATE_DATA_PROPERTY,
        property: property,
        value: value
    }
}

export function findRowByPropName(arr, name) {
    let ret = -1;
    for(let i = 0; i < arr.length; i++){
        if(arr[i].field === name){
            ret = arr[i];
            break;
        }
    }

    return ret;
}
