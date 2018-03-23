Number.prototype.formatSize = function () {
    var sizeText;
    if (this.toString() / 1024 / 1024 / 1024 > 1) {
        sizeText = parseFloat(this.toString() / 1024 / 1024 / 1024).toFixed(2) + "Gb";
    } else if (this.toString() / 1024 / 1024 > 1) {
        sizeText = parseFloat(this.toString() / 1024 / 1024).toFixed(2) + "Mb";
    } else if (this.toString() / 1024 > 1) {
        sizeText = parseFloat(this.toString() / 1024).toFixed(2) + "Kb";
    } else {
        sizeText = parseFloat(this.toString()).toFixed(2) + "b";
    }
    return sizeText;
}

var GnifFileUpload = (function () {

    var buildFormData = function (obj) {
        var formData = new FormData();
        for (var item in obj) {
            if (obj.hasOwnProperty(item)) {
                formData.append(item, obj[item]);
            }
        }
        return formData;
    }

    function flashVersion() {
        var version;
        try {
            version = navigator.plugins['Shockwave Flash'];
            version = version.description;
        } catch (ex) {
            try {
                version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                    .GetVariable('$version');
            } catch (ex2) {
                version = '0.0';
            }
        }
        version = version.match(/\d+/g);
        return parseFloat(version[0] + '.' + version[1], 10);
    }


    function updateProgress(fileList, file, percentage) {
        var progBar = $('#' + fileList).find('.' + file.id);
        var pg = progBar.find(".progress-bar")[0];
        progBar.attr('aria-valuenow', percentage * 100);
        pg.style.width = percentage * 100 + "%";
        $('#' + fileList).find('.' + file.id + 'file-status').text('正在上传');
    }

    var uploadUrl;
    var deleteUrl;
    var system_code;
    var urls = {
        uploadSignature: './getFileUploadSignature.json?info='
    }


    function _init(swfUrl, hostUrl, filePickerDiv, fileListDiv, uploadBtn, pluginDiv, isMultiple, systemCode, uploadAllFunc, fileDatagrid, tmp, savePath, call) {
        var $uploadBtn = $("#" + uploadBtn);
        system_code = systemCode;
        uploadUrl = hostUrl + "upload.html";
        deleteUrl = hostUrl + "file_delete.html";
        if (!tmp)
            tmp = 0;

        if (!WebUploader.Uploader.support('flash') && WebUploader.browser.ie) {
            if (flashVersion()) {
                $.messager.alert('提示', "flash版本过低，上传控件无法使用", 'info');
            } else {
                $.messager.alert('提示', "没有安装flash，上传控件无法使用", 'info');
            }
            return;
        } else if (!WebUploader.Uploader.support()) {
            $.messager.alert('提示', "上传控件不支持当前浏览器，请换一个浏览器后重试", 'info');
            return;
        }


        $("#" + fileListDiv).datagrid({
            columns: [[
                {field: 'fileId', title: '文件id', width: 0, hidden: true},
                {field: 'fileName', title: '文件', width: 170},
                {field: 'fileSize', title: '文件大小', width: 120},
                {
                    field: 'fileProgress', title: '上传进度', width: 250, align: 'center', height: 30,
                    formatter: function (value, row, index) {
                        var returnStr = '<div class="progress ' + row.fileId + '"><div class="progress-bar" style="width: 0%"></div></div>';
                        return returnStr;
                    }
                },
                {
                    field: 'fileStatus', title: '上传状态', width: 105, align: 'center',
                    formatter: function (value, row, index) {
                        var returnStr = "<label class='" + row.fileId + "file-status'>" + value + "</label>";
                        return returnStr;
                    }
                },
                {
                    field: 'operate', title: '操作', width: 120,
                    formatter: function (value, row, index) {
                        var returnStr = "<a class='file-del-btn " + row.fileId + "del-btn' data-index='" + index + "' data-file='" + row.fileId + "' href='javascript:void(0)'>删除</a>";
                        return returnStr;
                    }
                }
            ]],
            title: '文件列表',
            fitColumns: true,
            rownumbers: true,
            nowrap: true,
            border: true
        });

        $("#" + fileListDiv).datagrid('fixRowHeight');


        WebUploader.Uploader.register({
            "before-send-file": "beforeSendFile",
            "before-send": "beforeSend",
            "after-send-file": "afterSendFile"
        }, {
            //时间点1：所有分块进行上传之前调用此函数
            beforeSendFile: function (file) {
                var deferred = WebUploader.Deferred(), files = uploader.getFiles(), flag = false;
                for (var i = 0; i < files.length; i++) {
                    if (flag = (files[i].id == file.id)) {
                        break;
                    }
                }
                if (!flag) {
                    return;
                }

                if (file.size >= 52428800) {//文件大于50M
                    // uploadUrl = hostUrl + "upload.html";
                    // this.owner.options.server = uploadUrl;
                    file.server = hostUrl + "upload.html";
                    (new WebUploader.Uploader()).md5File(file, 0, file.size).progress(function (percentage) {
                        $('#' + pluginDiv).find('.' + file.id + 'file-status').text('文件分析中');
                    }).then(function (val) {
                        file.md5 = val;
                        var param = JSON.parse(send_request(urls.uploadSignature, encodeURIComponent(encodeURIComponent(file.md5 + "\n" + file.name + "\n" + file.size))));
                        // $.jsonp({
                        //     async: true,
                        //     type: "POST",
                        //     url: uploadUrl,
                        //     callbackParameter: "callback",
                        //     callback: "uploadcallback",
                        //     data: {
                        //         code: systemCode,
                        //         signature: encodeURIComponent(param.signature),
                        //         fileInfo: encodeURIComponent(param.fileInfo),
                        //         tmp: tmp,
                        //         step: 1
                        //     },
                        //     success: function (response) {
                        //         if (response.isSuccess) {
                        //             if (!response.notRepeat) {
                        //                 //文件存在，跳过
                        //                 $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传成功');
                        //                 $('#' + pluginDiv).find('.' + file.id + 'del-btn').attr("data-id", response.attributes.fileNo);
                        //                 deferred.reject(response);
                        //                 //uploader.trigger('repeatUpload', response, file);
                        //             } else {
                        //                 deferred.resolve();
                        //             }
                        //         } else {
                        //             deferred.reject(response);
                        //         }
                        //     },
                        //     error: function (XMLHttpRequest, textStatus, errorThrown) {
                        //         uploader.stop(true);
                        //         deferred.reject();
                        //         $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传出错');
                        //         $.messager.alert('提示', "连接出错,请刷新页面，重试", 'error');
                        //     }
                        // });

                        fetch(uploadUrl, {
                            method: "POST",
                            mode: 'cors',
                            body: buildFormData({
                                code: systemCode,
                                signature: encodeURIComponent(param.signature),
                                fileInfo: encodeURIComponent(param.fileInfo),
                                tmp: tmp,
                                step: 1
                            }),
                        }).then(function (response) {
                            response.json().then(function (response) {
                                if (response.isSuccess) {
                                    if (!response.notRepeat) {
                                        //文件存在，跳过
                                        $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传成功');
                                        $('#' + pluginDiv).find('.' + file.id + 'del-btn').attr("data-id", response.attributes.fileNo);
                                        deferred.reject(response);
                                        //uploader.trigger('repeatUpload', response, file);
                                    } else {
                                        deferred.resolve();
                                    }
                                } else {
                                    deferred.reject(response);
                                }
                            });
                        }).catch(function (error) {
                            uploader.stop(true);
                            deferred.reject();
                            $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传出错');
                            $.messager.alert('提示', "连接出错,请刷新页面，重试", 'error');
                            console.error(error.message);
                        });

                    });
                } else {
                    // uploadUrl = hostUrl + "common_upload.html";
                    // this.owner.options.server = uploadUrl;
                    file.server = hostUrl + "common_upload.html";
                    (new WebUploader.Uploader()).md5File(file, 0, file.size).progress(function (percentage) {
                        $('#' + pluginDiv).find('.' + file.id + 'file-status').text('文件分析中');
                    }).then(function (val) {
                        file.md5 = val;
                        deferred.resolve();
                    });

                }
                return deferred.promise();
            },
            //时间点2：如果有分块上传，则每个分块上传之前调用此函数
            beforeSend: function (block) {
                var deferred = WebUploader.Deferred(), ow = this.owner, file = block.file, flag = false,
                    files = uploader.getFiles();
                for (var i = 0; i < files.length; i++) {
                    if (flag = (files[i].id == file.id)) {
                        break;
                    }
                }
                if (!flag) {
                    return;
                }
                this.owner.options.server = file.server;
                if (file.size >= 52428800) {
                    (ow).md5File(block.file, block.chunk * ow.options.chunkSize, block.chunk * ow.options.chunkSize + parseInt(block.end - block.start)).then(function (val) {
                        // $.jsonp({
                        //     type: "POST",
                        //     url: uploadUrl,
                        //     callbackParameter: "callback",
                        //     callback: "uploadcallback",
                        //     data: {
                        //         //文件名称
                        //         fileName: block.file.name,
                        //         //文件md5
                        //         fileMd5: block.file.md5,
                        //         //文件大小
                        //         fileSize: block.file.size,
                        //         //当前分块名称，即起始位置
                        //         chunkOrder: block.chunk != 0 ? block.chunk : block.chunks,
                        //         //当前分块大小
                        //         chunkSize: block.end - block.start,
                        //         step: 2
                        //     },
                        //     success: function (response) {
                        //         if (response.isSuccess) {
                        //             if (!response.notRepeat) {
                        //                 //文件块存在，跳过
                        //                 deferred.reject();
                        //             } else {
                        //                 //分块不存在或不完整，重新发送该分块内容
                        //                 deferred.resolve();
                        //             }
                        //         } else {
                        //             //如果是文件出错，则停止上传
                        //             uploader.trigger('uploadError', file, response);
                        //         }
                        //     },
                        //     error: function (XMLHttpRequest, textStatus, errorThrown) {
                        //         deferred.reject();
                        //         uploader.stop(true);
                        //         $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传出错');
                        //         $.messager.alert('提示', "连接出错,请刷新页面，重试", 'error');
                        //     }
                        // });


                        fetch(uploadUrl, {
                            method: "POST",
                            mode: 'cors',
                            body: buildFormData({
                                //文件名称
                                fileName: block.file.name,
                                //文件md5
                                fileMd5: block.file.md5,
                                //文件大小
                                fileSize: block.file.size,
                                //当前分块名称，即起始位置
                                chunkOrder: block.chunk != 0 ? block.chunk : block.chunks,
                                //当前分块大小
                                chunkSize: block.end - block.start,
                                step: 2
                            }),
                        }).then(function (response) {
                            response.json().then(function (response) {
                                if (response.isSuccess) {
                                    if (!response.notRepeat) {
                                        //文件块存在，跳过
                                        deferred.reject();
                                    } else {
                                        //分块不存在或不完整，重新发送该分块内容
                                        deferred.resolve();
                                    }
                                } else {
                                    //如果是文件出错，则停止上传
                                    uploader.trigger('uploadError', file, response);
                                }
                            });
                        }).catch(function (error) {
                            deferred.reject();
                            uploader.stop(true);
                            $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传出错');
                            $.messager.alert('提示', "连接出错,请刷新页面，重试", 'error');
                            console.error(error.message);
                        });

                        //ow.options.formData.fileMd5 = block.file.md5;
                        ow.options.formData.chunk = block.chunk != 0 ? block.chunk : block.chunks;
                        ow.options.formData.chunkSize = block.end - block.start;
                        ow.options.formData.chunkMd5 = val;
                    });
                } else {
                    deferred.resolve();
                }
                return deferred.promise();

            },
            //时间点3：所有分块上传成功后调用此函数
            afterSendFile: function (file) {
                var flag = false, files = uploader.getFiles();
                for (var i = 0; i < files.length; i++) {
                    if (flag = (files[i].id == file.id)) {
                        break;
                    }
                }
                if (!flag) {
                    return;
                }
                var deferred = WebUploader.Deferred();
                if (file.size >= 52428800) {
                    //如果分块上传成功，则通知后台合并分块
                    var data = {
                        //文件名称
                        fileName: file.name,
                        //文件唯一标记
                        fileMd5: file.md5,
                        //文件大小
                        fileSize: file.size,
                        step: 4,
                        code: systemCode,
                        tmp: tmp
                    };

                    if (call) {
                        data.call = encodeURIComponent(CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(JSON.stringify(call))));
                    }

                    if (savePath) {
                        data.savepath = savePath;
                    }


                    // $.jsonp({
                    //     type: "POST",
                    //     url: uploadUrl,
                    //     callbackParameter: "callback",
                    //     callback: "uploadcallback",
                    //     data: data,
                    //     success: function (response) {
                    //         if (response.isSuccess) {
                    //             $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传成功');
                    //             $('#' + pluginDiv).find('.' + file.id + 'del-btn').attr("data-id", response.attributes.fileNo);
                    //             changeStatus($uploadBtn, uploader);
                    //             if (uploadAllFunc)
                    //                 uploadAllFunc(response, file);
                    //             deferred.resolve();
                    //         } else {
                    //             updateProgress(pluginDiv, file, 0);
                    //             uploader.trigger('uploadError', file, response);
                    //             changeStatus($uploadBtn, uploader);
                    //         }
                    //     },
                    //     error: function (XMLHttpRequest, textStatus, errorThrown) {
                    //         uploader.stop(true);
                    //         deferred.reject();
                    //         $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传出错');
                    //         $.messager.alert('提示', "连接出错,请刷新页面，重试", 'error');
                    //     }
                    // });

                    fetch(uploadUrl, {
                        method: "POST",
                        mode: 'cors',
                        body: buildFormData(data),
                    }).then(function (response) {
                        response.json().then(function (response) {
                            if (response.isSuccess) {
                                $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传成功');
                                $('#' + pluginDiv).find('.' + file.id + 'del-btn').attr("data-id", response.attributes.fileNo);
                                changeStatus($uploadBtn, uploader);
                                if (uploadAllFunc)
                                    uploadAllFunc(response, file);
                                deferred.resolve();
                            } else {
                                updateProgress(pluginDiv, file, 0);
                                uploader.trigger('uploadError', file, response);
                                changeStatus($uploadBtn, uploader);
                            }
                        });
                    }).catch(function (error) {
                        uploader.stop(true);
                        deferred.reject();
                        $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传出错');
                        $.messager.alert('提示', "连接出错,请刷新页面，重试", 'error');
                        console.error(error.message);
                    })
                } else {
                    deferred.resolve();
                }
                return deferred.promise();
            }
        });


        var uploader = WebUploader.create({
            swf: swfUrl,
            server: '',
            chunked: true,
            chunkSize: 50 * 1024 * 1024,
            pick: {
                id: '#' + filePickerDiv,
                multiple: !!!isMultiple ? isMultiple : true
            },
            resize: false,
            method: 'POST',
            prepareNextFile: true,
            threads: 3,
            compress: false
        });

        uploader.id = fileListDiv;
        uploader.list = $('#' + fileListDiv);


        $('#' + filePickerDiv).unbind();
        $('#' + pluginDiv + ' .webuploader-pick').click(function () {
            $('#' + pluginDiv + " .webuploader-element-invisible").trigger("click");
        });


        //当有文件被添加进队列的时候
        uploader.on('fileQueued', function (file) {
            var sizeText = file.size.formatSize();

            $("#" + fileListDiv).datagrid('appendRow', {
                fileId: file.id,
                fileName: file.name,
                fileSize: sizeText,
                fileStatus: "等待上传"
            });

            $("#" + pluginDiv).find("div[class$='-fileProgress']").attr("style", "height:25px;text-align:center;");

            $('#' + pluginDiv).find(".file-del-btn").unbind();

            //点击删除，删除文件显示和上传队列中的文件，调用文件服务删除接口，删除文件，清空datagrid中文件
            $('#' + pluginDiv).find(".file-del-btn").click(function () {
                //调用删除接口
                var thisBtn = $(this);
                var fileNo = thisBtn.attr("data-id");


                if (fileNo) {

                    var jsonData = JSON.parse(send_request(urls.uploadSignature, encodeURIComponent(fileNo + "\\n" + tmp)));
                    var data = {
                        policy: jsonData.fileInfo,
                        signature: jsonData.signature,
                        code: systemCode
                    };

                    // $.jsonp({
                    //     type: "POST",
                    //     url: deleteUrl,
                    //     callbackParameter: "callback",
                    //     callback: "filedeletecallback",
                    //     data: data,
                    //     success: function (response) {
                    //         if (response.isSuccess) {
                    //             //清空datagrid中文件
                    //             var fileGrid = $("#" + fileDatagrid);
                    //             if (fileGrid.attr("id")) {
                    //
                    //                 var item = fileGrid.datagrid('getRows');
                    //                 if (item) {
                    //                     for (var i = item.length - 1; i >= 0; i--) {
                    //                         if (item[i]['fileNo'] == fileNo) {
                    //                             var index = fileGrid.datagrid('getRowIndex', item[i]);
                    //                             fileGrid.datagrid('deleteRow', index);
                    //                         }
                    //                     }
                    //                 }
                    //
                    //             }
                    //
                    //             //删除文件显示
                    //
                    //             var item = $('#' + fileListDiv).datagrid('getRows');
                    //             if (item) {
                    //                 for (var i = item.length - 1; i >= 0; i--) {
                    //                     if (item[i]['fileId'] == thisBtn.attr("data-file")) {
                    //                         var index = $('#' + fileListDiv).datagrid('getRowIndex', item[i]);
                    //                         $('#' + fileListDiv).datagrid('deleteRow', index);
                    //                     }
                    //
                    //                 }
                    //             }
                    //
                    //
                    //             //删除上传队列中的文件
                    //             uploader.removeFile(thisBtn.attr("data-file"), true);
                    //
                    //         } else {
                    //             $.messager.alert('删除失败', response.message, 'error');
                    //         }
                    //     },
                    //     error: function (XMLHttpRequest, textStatus, errorThrown) {
                    //         $.messager.alert('删除失败', "与服务器连接出错，请重试", 'error');
                    //     }
                    // });


                    fetch(deleteUrl, {
                        method: "POST",
                        mode: 'cors',
                        body: buildFormData(data),
                    }).then(function (response) {
                        response.json().then(function (response) {
                            if (response.isSuccess) {
                                //清空datagrid中文件
                                var fileGrid = $("#" + fileDatagrid);
                                if (fileGrid.attr("id")) {

                                    var item = fileGrid.datagrid('getRows');
                                    if (item) {
                                        for (var i = item.length - 1; i >= 0; i--) {
                                            if (item[i]['fileNo'] == fileNo) {
                                                var index = fileGrid.datagrid('getRowIndex', item[i]);
                                                fileGrid.datagrid('deleteRow', index);
                                            }
                                        }
                                    }

                                }

                                //删除文件显示

                                var item = $('#' + fileListDiv).datagrid('getRows');
                                if (item) {
                                    for (var i = item.length - 1; i >= 0; i--) {
                                        if (item[i]['fileId'] == thisBtn.attr("data-file")) {
                                            var index = $('#' + fileListDiv).datagrid('getRowIndex', item[i]);
                                            $('#' + fileListDiv).datagrid('deleteRow', index);
                                        }

                                    }
                                }


                                //删除上传队列中的文件
                                uploader.removeFile(thisBtn.attr("data-file"), true);

                            } else {
                                $.messager.alert('删除失败', response.message, 'error');
                            }
                        });
                    }).catch( function (error) {
                        $.messager.alert('删除失败', "与服务器连接出错，请重试", 'error');
                        console.error(error.message);
                    })

                } else {
                    //删除文件显示

                    var item = $('#' + fileListDiv).datagrid('getRows');
                    if (item) {
                        for (var i = item.length - 1; i >= 0; i--) {
                            if (item[i]['fileId'] == thisBtn.attr("data-file")) {
                                var index = $('#' + fileListDiv).datagrid('getRowIndex', item[i]);
                                $('#' + fileListDiv).datagrid('deleteRow', index);
                            }
                        }
                    }

                    //删除上传队列中的文件
                    uploader.removeFile(thisBtn.attr("data-file"), true);
                }


            });
        });

        //文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            updateProgress(pluginDiv, file, percentage);
        });

        uploader.on('uploadError', function (file, response) {
            if (response && response.isSuccess) {
                updateProgress(pluginDiv, file, 1);
                $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传成功');
                changeStatus($uploadBtn, uploader);
                if (uploadAllFunc)
                    uploadAllFunc(response, file);
            } else {
                updateProgress(pluginDiv, file, 0);
                $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传失败');
                if (response && response.message) {
                    $.messager.alert('上传失败', response.message, 'error');
                } else {
                    if (response && response == "abort") {
                        $.messager.alert('上传失败', "未连接到服务器", 'error');
                    }
                }
                changeStatus($uploadBtn, uploader);
            }

        });


        uploader.on('uploadBeforeSend', function (block, data) {


            // file为分块对应的file对象。
            var file = block.file;
            // 修改data可以控制发送哪些携带数据。
            var chunkMd5 = data.chunkMd5;
            data.code = systemCode;

            this.options.server = file.server;

            if (file.size >= 52428800) {
                var param = JSON.parse(send_request(urls.uploadSignature, encodeURIComponent(encodeURIComponent(file.md5 + "\n" + file.name + "\n" + file.size + "\n" + chunkMd5 + "\n" + data.chunkSize))));
                //data.signature = encodeURIComponent(CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(CryptoJS.HmacSHA1("Tue 23 May 2017 00:53:45 GMT" + "\n" + data.fileMd5 + "\n" + file.name + "\n" + file.size + "\n" + chunkMd5 + "\n" + data.chunkSize, key))));
                //data.fileInfo = encodeURIComponent(CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse("Tue 23 May 2017 00:53:45 GMT" + "\n" + data.fileMd5 + "\n" + file.name + "\n" + file.size + "\n" + chunkMd5 + "\n" + data.chunkSize)));
                data.step = 3;
                data.signature = encodeURIComponent(param.signature);
                data.fileInfo = encodeURIComponent(param.fileInfo);

            } else {
                var param = JSON.parse(send_request(urls.uploadSignature, encodeURIComponent(encodeURIComponent(file.md5 + "\n" + file.name + "\n" + file.size))));
                //data.signature = encodeURIComponent(CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(CryptoJS.HmacSHA1("Tue 23 May 2017 00:53:45 GMT" + "\n" + data.fileMd5 + "\n" + file.name + "\n" + file.size, key))));
                //data.fileInfo = encodeURIComponent(CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse("Tue 23 May 2017 00:53:45 GMT" + "\n" + data.fileMd5 + "\n" + file.name + "\n" + file.size)));
                data.signature = encodeURIComponent(param.signature);
                data.fileInfo = encodeURIComponent(param.fileInfo);
                data.tmp = tmp;
                if (call) {
                    data.call = encodeURIComponent(CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(JSON.stringify(call))));
                }

                if (savePath) {
                    data.savepath = savePath;
                }
            }
        });


        uploader.on('uploadAccept', function (object, response) {//上传中
            var deferred = WebUploader.Deferred();
            if (!response.isSuccess) {
                //显示错误信息
                $('#' + pluginDiv).find('.' + object.id + 'file-status').text('上传失败');
                $.messager.alert('上传失败', response.message, 'error');
                return false;
            } else {
                deferred.resolve();
            }
        });


        uploader.on('uploadSuccess', function (file, response) {//上传成功
            if (!response.isSuccess) {
                uploader.trigger('uploadError', file, response);
                return;
            }
            if (file.size <= 52428800) {
                $('#' + pluginDiv).find('.' + file.id + 'del-btn').attr("data-id", response.attributes.fileNo);
                $('#' + pluginDiv).find('.' + file.id + 'file-status').text('上传成功');
                changeStatus($uploadBtn, uploader);
                if (uploadAllFunc)
                    uploadAllFunc(response, file);
            }
        });


        uploader.on('repeatUpload', function (response, file) {//上传重复
            changeStatus($uploadBtn, uploader);
            if (uploadAllFunc)
                uploadAllFunc(response, file);
        });

        //初始化上传按钮事件
        $("#" + uploadBtn).unbind('click').click(function () {
            _start($("#" + uploadBtn), uploader);
        });

        return uploader;
    }


    function deleteFile(code, fileNo, callBack) {
        var jsonData = JSON.parse(send_request(urls.uploadSignature, encodeURIComponent(fileNo + "\\n1")));
        var data = {
            policy: jsonData.fileInfo,
            signature: jsonData.signature,
            code: code
        };

        // $.jsonp({
        //     type: "POST",
        //     url: deleteUrl,
        //     callbackParameter: "callback",
        //     callback: "filedeletecallback",
        //     data: data,
        //     success: function (response) {
        //         if (response.isSuccess) {
        //             if (callBack)
        //                 callBack();
        //         } else {
        //             $.messager.alert('删除失败', response.message, 'error');
        //         }
        //     }
        // });

        fetch(deleteUrl, {
            method: "POST",
            mode: 'cors',
            body: buildFormData(data),
        }).then(function (response) {
            response.json().then(function (response) {
                if (response.isSuccess) {
                    if (callBack)
                        callBack();
                } else {
                    $.messager.alert('删除失败', response.message, 'error');
                }
            });
        }).catch(function (error) {
            $.messager.alert('删除失败', error.message, 'error');
            console.error(error.message);
        })
    }


    function send_request(url, param) {
        var xmlhttp = null;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
        else if (window.ActiveXObject) {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }

        if (xmlhttp != null) {
            var serverUrl = url + param;
            xmlhttp.open("GET", serverUrl, false);
            xmlhttp.send(null);
            return xmlhttp.responseText;
        } else {
            $.messager.alert('提示', "Your browser does not support XMLHTTP.", 'info');
        }
    }


    function _stop(btn, ur) {
        ur.stop(true);
        btn.unbind('click').click(function () {
            _start(btn, ur);
        });
        btn.text("继续上传");
    }

    function _start(btn, ur) {
        if (ur.getFiles().length == 0) {
            $.messager.alert('提示', "请选择文件", 'info');
            return;
        }
        ur.upload();
        btn.unbind('click').click(function () {
            _stop(btn, ur);
        });
        btn.text("取消上传");
    }

    function _getFiles(ur) {
        if (!!ur) {
            return ur.getFiles();
        }
    }

    function _getUploadedFiles() {
        return 0;
    }

    function changeStatus($btn, ur) {
        $btn.unbind();
        $btn.click(function () {
            _start($btn, ur);
        });
        $btn.text("继续上传");
    }

    function uploaderRemove(ur, fileId) {
        ur.removeFile(fileId, true);
    }

    function btnInit(btn, ur) {
        $("#" + btn).text("开始上传");
        $("#" + btn).unbind('click').click(function () {
            _start($("#" + btn), ur);
        });
    }


    function changeTmp(attchFileGrid, ossServer, ossCode, tmp) {

        var fileRows = $('#' + attchFileGrid).datagrid("getRows");
        if (fileRows.length != 0) {

            var fileNos = '';
            $.each(fileRows, function (i, n) {
                fileNos += n.fileNo + ",";
            });
            var fileNoParam = fileNos.substring(0, fileNos.length - 1);


            var signatureAttr = JSON.parse(send_request("./getFileUploadSignature.json?info=", encodeURIComponent(tmp + "\\n" + fileNoParam)));

            // $.jsonp({
            //     async: true,
            //     type: "POST",
            //     url: ossServer + 'change_tmp.html',
            //     callbackParameter: "callback",
            //     callback: "uploadcallback",
            //     data: {
            //         code: ossCode,
            //         signature: signatureAttr.signature,
            //         policy: signatureAttr.fileInfo
            //     },
            //     success: function (response) {
            //         if (!response.isSuccess) {
            //             $.messager.alert('提示', "上传附件为临时文件，将被定时清除", 'info');
            //         }
            //     },
            //     error: function (XMLHttpRequest, textStatus, errorThrown) {
            //         $.messager.alert('提示', "上传附件为临时文件，将被定时清除", 'info');
            //     }
            // });

            fetch(ossServer + 'change_tmp.html?' + 'code=' + ossCode + '&signature=' + signatureAttr.signature + '&policy=' + signatureAttr.fileInfo, {
                method: "GET",
                mode: 'cors'
            }).then(function (response) {
                response.json().then(function (response) {
                    if (!response.isSuccess) {
                        $.messager.alert('提示', "上传附件为临时文件，将被定时清除", 'info');
                    }
                });
            }).catch(function (error) {
                $.messager.alert('提示', "上传附件为临时文件，将被定时清除", 'info');
                console.error(error.message);
            })

        }
    }

    return {
        init: _init,
        start: _start,
        stop: _stop,
        getFiles: _getFiles,
        getUploadedFiles: _getUploadedFiles,
        sendRequest: send_request,
        deleteFile: deleteFile,
        removeFileFromQueue: uploaderRemove,
        btnInit: btnInit,
        changeTmp: changeTmp
    };

})();