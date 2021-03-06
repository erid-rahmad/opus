import WatchListMixin from '@/core/application-dictionary/mixins/WatchListMixin';
import settings from '@/settings';
import { AccountStoreModule as accountStore } from '@/shared/config/store/account-store';
import { ElTable } from 'element-ui/types/table';
import { mixins } from 'vue-class-component';
import { Component, Watch } from 'vue-property-decorator';
import ContextVariableAccessor from "../../../ContextVariableAccessor";


@Component
export default class PaymentStatus extends mixins(ContextVariableAccessor, WatchListMixin) {
  gridSchema = {
    defaultSort: {},
    emptyText: 'No Records Found',
    maxHeight: 450,
    height: 420
  };

  public itemsPerPage = 10;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  private baseApiUrl = "/api/m-verifications";
  private baseApiUrlReference = "/api/ad-references";
  private baseApiUrlReferenceList = "/api/ad-reference-lists";

  private filterQuery: string = '';
  processing = false;

  public gridData: Array<any> = [];
  private totalAmount: number = null;

  selectedRow: any = {};
  public documentStatusOptions: any[] = [];
  public paymentStatusOptions: any[] = [];

  public dialogConfirmationVisible: boolean = false;
  public filter: any = {};
  public radioSelection: number = null;
  confirmReopen: boolean = false;

  get canReopen() {
    return this.selectedRow?.apReversed && this.selectedRow?.documentStatus !== 'ROP';
  }

  get dateDisplayFormat() {
    return settings.dateDisplayFormat;
  }

  get dateValueFormat() {
    return settings.dateValueFormat;
  }

  created(){
    this.initDocumentStatusOptions();
    this.initPaymentStatusOptions();
  }

  mounted() {
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

  rowClassName({row}) {
    if (row.documentStatus !== 'CNL' && row.receiptReversed) {
      return 'danger-row';
    }

    return '';
  }

  public clear(): void {
    this.page = 1;
    this.retrieveAllRecords();
  }

  public singleSelection (row) {
    this.radioSelection = this.gridData.indexOf(row);
    this.selectedRow = row;
  }

  public reopenDocument() {
    const data = { ...this.selectedRow };
    data.verificationStatus = 'ROP';

    this.dynamicWindowService(this.baseApiUrl)
      .update(data)
      .then(() => {
        const message = 'Invoice verification has been reopened'

        this.retrieveAllRecords();
        this.$message({
          message: message,
          type: 'success'
        });
      })
      .catch(err => {
        this.$message({
          message: err.response?.data?.detail || err.message,
          type: 'error'
        });
      })
      .finally(() => {
        this.processing = false;
        this.confirmReopen = false;
      });
  }

  public retrieveAllRecords(): void {
    this.clearSelection();
    this.processing = true;
    const paginationQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    };

    const watchListQuery = this.getWatchListQuery();

    if (watchListQuery) {
      watchListQuery.split('&').forEach(field => {
        const key = field.substring(0, field.indexOf('.'));
        const value = field.substring(field.indexOf('=') + 1);
        this.$set(this.filter, key, value);
      });
    }

    this.dynamicWindowService(this.baseApiUrl)
      .retrieve({
        criteriaQuery: [
          this.filterQuery,
          watchListQuery,
          `vendorId.equals=${accountStore.userDetails.cVendorId}`
        ],
        paginationQuery
      })
      .then(res => {
        this.gridData = res.data.map((item: any) => {
          item.totalAmount = parseInt(item.totalLines) + parseInt(item.taxAmount);
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
        this.radioSelection = null;
        this.selectedRow = {};
      });
  }

  private clearSelection() {
    (<ElTable>this.$refs.mainTable)?.setCurrentRow();
    this.radioSelection = null;
    this.selectedRow = null;
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
    this.retrieveAllRecords();
  }

  private initDocumentStatusOptions() {
    this.dynamicWindowService(null)
      .retrieveReferenceLists('docStatus')
      .then(res => {
        this.documentStatusOptions = res.map(item => 
          ({
              key: item.value,
              label: item.name
          })
        );
      });
  }

  private initPaymentStatusOptions() {
    this.dynamicWindowService(null)
      .retrieveReferenceLists('paymentStatus')
      .then(res => {
        this.paymentStatusOptions = res.map(item => 
          ({
              key: item.value,
              label: item.name
          })
        );
      });
  }

  public verificationFilter(){

    this.filterQuery = "";

    if((this.filter.verificationNo != null)&&(this.filter.verificationNo != "")){
      this.filterQuery = "verificationNo.equals="+this.filter.verificationNo;
    }
    if((this.filter.invoiceNo != null)&&(this.filter.invoiceNo != "")){
      if(this.filterQuery != ""){
        this.filterQuery += "&"
      }
      this.filterQuery += "invoiceNo.equals="+this.filter.invoiceNo;
    }
    if((this.filter.verificationStatus != null)&&(this.filter.verificationStatus != "")){
      if(this.filterQuery != ""){
        this.filterQuery += "&"
      }
      this.filterQuery += "verificationStatus.equals="+this.filter.verificationStatus;
    }

    if((this.filter.verificationDate != null)&&(this.filter.verificationDate != "")){
      if(this.filterQuery != ""){
        this.filterQuery += "&"
      }
      this.filterQuery += "verificationDate.lessOrEqualThan="+this.filter.verificationDate;
    }
    if((this.filter.invoiceDate != null)&&(this.filter.invoiceDate != "")){
      if(this.filterQuery != ""){
        this.filterQuery += "&"
      }
      this.filterQuery += "invoiceDate.lessOrEqualThan="+this.filter.invoiceDate;
    }
    if((this.filter.payStatus != null)&&(this.filter.payStatus != "")){
      if(this.filterQuery != ""){
        this.filterQuery += "&"
      }
      this.filterQuery += "payStatus.equals="+this.filter.payStatus;
    }

    this.retrieveAllRecords();
  }

  formatDocumentStatus(value: string) {
    return this.documentStatusOptions.find(status => status.key === value)?.label || value;
  }

  formatPaymentStatus(value: string) {
    return this.paymentStatusOptions.find(status => status.key === value)?.label || value;
  }

}
