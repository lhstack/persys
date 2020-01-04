function request(options){
   return new Promise(function (resolve,reject){
       $.ajax({
           url: options.url,
           type: options.method,
           data: options.data || {},
           dataType: options.dataType || "json",
           headers: options.headers || {},
           success: function(res){
               resolve(res);
           },
           error: function(err){
               reject(err);
           }
       })
   })
}
function get(options){
    options.method = "GET";
    return request(options);
}
function post(options){
    options.method = "POST";
    return request(options);
}

function requestDel(options){
    options.method = "DELETE";
    return request(options);
}
function update(options){
    options.method = "PUT";
    return request(options);
}
