import $ from 'jquery'

const proHost = window.location.protocol + "//" + window.location.host;
const href = window.location.href.split("bpmnjs")[0];
const key = href.split(window.location.host)[1];
const publicurl = proHost + key;
const tool = {

    downLoad(bpmnModeler) {

        var downloadLink = $("#download_BPMN")
        bpmnModeler.saveXML({format: true}, function (err, xml) {

            if (err) {
                return console.error("could not save bpmn", err)
            }

            tool.setEncoded(downloadLink, "diagram.bpmn", err ? null : xml)
        });

    },
    saveBPMN(bpmnModeler) {

        bpmnModeler.saveXML({format: true}, function (err, xml) {

            if (err) {
                return console.error("could not save bpmn", err)
            }
            console.log(xml);
            var param = {
                "stringBPMN": xml
            }
            $.ajax({
                url: publicurl + 'processDefinition/addDeploymentByString',
                type: 'POST',
                dataType: 'json',
                data: param,
                success: function (result) {
                    if (result.status == '0') {
                        alert('BPMN部署成功');
                    } else {
                        alert(result.msg);
                    }
                },
                error: function (err) {
                    alert(err)
                }
            })
        });

    },
    // 上传并查看BPMN
    uploadBPMN(bpmnModeler) {
        var FileUpload = document.myForm2.uploadFile2.files[0]
        var fm = new FormData();
        fm.append("processFile", FileUpload);
        $.ajax({
            url: publicurl + 'processDefinition/upload',
            type: 'POST',
            data: fm,
            async: false,
            contentType: false,
            processData: false,
            success: function (result) {
                if (result.status == '0') {
                    // alert('BPMN部署成功');
                    var url = publicurl + 'bpmn/' + result.obj;
                    tool.openBPMN_URL(bpmnModeler, url);
                } else {
                    alert(result.msg);
                }
            },
            error: function (err) {
                alert(err)
            }
        })

    },

    openBPMN_URL(bpmnModeler, url) {
        $.ajax(url,{dataType: 'text'}).done(async function (xml){
            try {
                await bpmnModeler.importXML(xml);
            } catch (err) {
                console.error(err);
            }
        })
    },

    setEncoded(link, name, data) {
        var encodedData = encodeURIComponent(data);
        if (data) {

            link.addClass('active').attr({
                'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
                'download': name
            });
        } else {
            link.removeClass('active');
        }
    }


}
export default tool