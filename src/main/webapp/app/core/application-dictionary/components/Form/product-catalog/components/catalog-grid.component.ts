import settings from '@/settings';
import AlertMixin from '@/shared/alert/alert.mixin';
import Vue from 'vue';
import { mixins } from 'vue-class-component';
import { Component, Watch } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import ContextVariableAccessor from "../../../ContextVariableAccessor";

const CatalogGridProp = Vue.extend({
  props: {
    status: {
      type: String,
      default: () => ''
    },
  }
})
@Component
export default class CatalogGrid extends mixins(Vue2Filters.mixin, AlertMixin, ContextVariableAccessor, CatalogGridProp) {
  gridSchema = {
    defaultSort: {},
    emptyText: 'No Records Found',
    maxHeight: 450,
    height: 450
  };

  public itemsPerPage = 10;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;
  public statusCatalog = "";

  private baseApiUrl = "/api/m-product-catalogs";

  private filterQuery: string = '';
  private processing = false;

  public gridData: Array<any> = [];

  selectedRows: Array<any> = [];

  get dateDisplayFormat() {
    return settings.dateDisplayFormat;
  }

  get dateValueFormat() {
    return settings.dateValueFormat;
  }

  created() {
    console.log(this.status)
  }

  public mounted(): void {

    if (this.status == '0') {
      this.filterQuery = "";
    } else {
      if(this.status == '1'){
        this.statusCatalog = "DRF";
      }else  if(this.status == '2'){
        this.statusCatalog = "LVE";
      }else {
        this.statusCatalog = "BLK";
      }
      this.filterQuery = "documentStatus.equals="+this.statusCatalog;
    }
    this.retrieveAllRecords();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder.prop;
    this.reverse = propOrder.order === 'ascending';
    const {propOrder: property, reverse} = this;
    this.$emit('order-changed', { property, reverse });
    this.transition();
  }

  public changePageSize(size: number) {
    this.itemsPerPage = size;
    if(this.page!=1){
      this.page = 0;
    }
    this.retrieveAllRecords();
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  @Watch('page')
  onPageChange(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.retrieveAllRecords();
  }

  public refreshCatalogGrid(){
    this.selectedRows = [];
    this.retrieveAllRecords();
  }

  public clear(): void {
    this.page = 1;
    this.retrieveAllRecords();
  }

  public onSelectionChanged(value: any) {
    this.selectedRows = value;
    this.$emit("selectedRows", this.selectedRows);
    console.log(value);
  }

  private close(){
    //this.dialogConfirmationVisible = false;
    this.selectedRows = [];
    this.retrieveAllRecords();
  }

  public retrieveAllRecords(): void {
    if (!this.baseApiUrl) {
      return;
    }

    this.processing = true;
    const paginationQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    };

    this.dynamicWindowService(this.baseApiUrl)
      .retrieve({
        criteriaQuery: this.filterQuery,
        paginationQuery
      })
      .then(res => {
        console.log(res);

        this.gridData = res.data.map((item: any) => {
          if(item.cGallery != null){
            if(item.cGallery.cGalleryItems.length){
              console.log(item.cGallery.cGalleryItems.length);
							item.imgList = item.cGallery.cGalleryItems;
            }
          }

          return item;
        });

        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
        this.$emit('total-count-changed', this.queryCount);

      })
      .catch(err => {
        console.error('Failed getting the record. %O', err);
        this.$message({
          type: 'error',
          message: err.detail || err.message
        });
      })
      .finally(() => {
        this.processing = false;
        this.selectedRows = [];
      });
  }

  displayImage(imgList){
    var x = 0;
    var img = "";

    for(x; x<imgList.length; x++){
      if(imgList[x].preview){
        img = `http://localhost:9000/api/c-attachments/download/${imgList[x].cAttachment.id}-${imgList[x].cAttachment.fileName}`;
      }
    }

    return img;
  }

  displayImageList(imgList){
    var x = 0;
    var arr = [];

    for(x; x<imgList.length; x++){
      arr.push(`http://localhost:9000/api/c-attachments/download/${imgList[x].cAttachment.id}-${imgList[x].cAttachment.fileName}`);
    }

    return arr;
  }

}
