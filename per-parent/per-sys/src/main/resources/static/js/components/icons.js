const iconsTemplate = {
    name:"iconsTemplate",
    data:function(){
        return {
            id:"",
            icon: "未选中图标"
        };
    },
    props:{
        icons:{
            type: Array,
            default: function(){
                return []
            }
        },
        iconStyle:{
            type: String,
            default: "background: none; border:1px solid #409eff;cursor: pointer;margin: 5px"
        },
        pageSize:{
            type: Number,
            default: 60
        },
        parentDivStyle:{
            type: Object,
            default: function(){
                return {
                    width: "500px",
                }
            }
        },
        noActiveStyle:{
            type: Object,
            default: function(){
                return {
                    color: "#409eff",
                    background: "rgba(1,1,1,0)",
                    border: "1px solid #409eff",
                    cursor: "pointer",
                    margin: "5px"
                }
            }
        },
        isActiveStyle:{
            type: Object,
            default: function(){
                return {
                    color: "red",
                    background: "rgba(1,1,1,0)",
                    border: "1px solid red",
                    cursor: "pointer",
                    margin: "5px"
                }
            }
        },
        customStyle:{
            type: Object/String,
            default: function(){
                return "background:#cac9c9;"
            }
        },
        carHeight:{
            type: String,
            default: "300px"
        },
        dialogVisible:{
            type: Boolean,
            defalut: false
        }
    },
    template:  `
            <div :style="parentDivStyle">
                    <el-dialog
                    title="图标选择"
                    :visible.sync="dialogVisible"
                    width="540px"
                    @close="closeResult">
                    <el-tag type="success" v-text="icon" style="margin-bottom:2px;"></el-tag>
                    <el-carousel :height="carHeight" :autoplay="false" arrow="always">
                        <el-carousel-item v-for="item,parentIndex in totalPage" :style="customStyle" :key="item" >
                            <avue-avatar  v-for="icon,index in getIcons(item)"  :key="index" size="large" :style="id == idResult(item,icon,index) ? isActiveStyle : noActiveStyle">
                                <i :class="icon" @click.stop="handlerClick(icon,idResult(item,icon,index))" :id="idResult(item,icon,index)"></i>
                            </avue-avatar>       
                        </el-carousel-item>
                    </el-carousel>     
                    <div style="margin-left: 325px;margin-top:10px">
                        <slot name="footer" :icon="icon"></slot>
                    </div>
                    </el-dialog>
            </div>
        `,
    methods:{
        getIcons:function(item){
            var cloneIcons = JSON.parse(JSON.stringify(this.icons))
            return cloneIcons.slice((item - 1) * this.pageSize,item * this.pageSize)
        },
        handlerClick:function(icon,id){
            this.id = id;
            this.icon = icon;
            this.$emit("handler-click-result",icon)
        },
        idResult:function(item,icon,index){
            var id = item + icon + index;
            return id.replace(" ","");
        },
        closeResult:function(){
            this.$emit("handler-close-rollback");
        }
    },
    computed:{
        totalPage:function(){
            return this.icons.length % this.pageSize != 0 ? parseInt(this.icons.length / this.pageSize) + 1 : this.icons.length / this.pageSize;
        }
    }
};