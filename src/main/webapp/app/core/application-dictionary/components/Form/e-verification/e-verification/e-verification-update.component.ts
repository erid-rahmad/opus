import settings from '@/settings';
import AlertMixin from '@/shared/alert/alert.mixin';
import { AccountStoreModule as accountStore } from '@/shared/config/store/account-store';
import Vue from 'vue';
import { mixins } from 'vue-class-component';
import { Component, Watch } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import ContextVariableAccessor from "../../../ContextVariableAccessor";
import MatchPo from './match-po.vue';
import { ElForm } from 'element-ui/types/form';

const EVerificationUpdateProps = Vue.extend({
  props: {
    formUpdate: {
      type: Object,
      default: () => {}
    },

  }
})

@Component({
  components: {
    MatchPo
  }
})
export default class EVerificationUpdate extends mixins(Vue2Filters.mixin, AlertMixin, ContextVariableAccessor, EVerificationUpdateProps) {
  gridSchema = {
    defaultSort: {},
    emptyText: 'No Records Found',
    maxHeight: 470,
    height: 450
  };
  rules = {
    taxInvoice: [
      { min: 15, message: 'Length should be 15' }
    ]
  }

  public itemsPerPage = 10;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  private totalItems = 0;

  private baseApiUrlEVerification = "/api/m-verifications";
  private baseApiUrlEVerificationLine = "/api/m-verification-lines";
  private baseApiUrlCurrency = "/api/c-currencies";
  public gridData: Array<any> = [];
  public removedLines: Array<any> = [];

  private processing = false;
  private fullscreenLoading: boolean = false;

  public currencyOptions: any = {};

  public dialogMatchPoVisible: boolean = false;
  private totalAmount: number = null;

  public matchPo = {};
  public modeFilterMatchPo: any = {};
  private filterQuery: string = "";

  eVerification = {
    form: {},
    line: [],
    remove: []
  }

  get dateDisplayFormat() {
    return settings.dateDisplayFormat;
  }

  get dateValueFormat() {
    return settings.dateValueFormat;
  }

  created(){
    this.retrieveGetCurrencies();
    this.formUpdate.vendorId = accountStore.userDetails.cVendorId;
    this.formUpdate.vendorName = accountStore.userDetails.cVendorName;

    if (this.formUpdate.id) {
      this.filterQuery = `verificationId.equals=${this.formUpdate.id}`;
      this.retrieveEVerificationLine();
    }
  }

  changeOrder(propOrder): void {
    this.propOrder = propOrder.prop;
    this.reverse = propOrder.order === 'ascending';
    const {propOrder: property, reverse} = this;
    this.$emit('order-changed', { property, reverse });
    this.transition();
  }

  sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  // =====================================
  // Pagination
  changePageSize(size: number) {
    this.itemsPerPage = size;
    if(this.page!=1){
      this.page = 0;
    }
    this.transition();
  }

  loadPage(page: number): void {
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

  transition(): void {
    if (this.formUpdate.id) {
      this.filterQuery = `verificationId.equals=${this.formUpdate.id}`;
      this.retrieveEVerificationLine();
    }
  }
  // =====================================

  public displayMatchPo(id){
    var width, titleModal;
    if(id == 1){
      width = "90%";
      titleModal = "Receipt Order";
    }else{
      width = "40%";
      titleModal = "Add By Receipt No";
    }
    this.modeFilterMatchPo = {
      mode: id,
      width: width,
      titleModal: titleModal,
      filterByReceiptNo: ''
    };
    this.dialogMatchPoVisible = true;
  }

  public closeEVerificationUpdate() {
    this.$emit('close-e-verification-update');
  }

  updateEVerification(){
    var message;
    if(this.gridData.length){

      (this.$refs.eVerificationUpdate as ElForm).validate((passed, errors) => {
        if (passed) {
          this.fullscreenLoading = true;
          setTimeout(() => {
            this.submit();
          }, 2000);

        } else {
          return false;
        }
      })

    }else{
      message = "Please input receipt order";
      this.notifWarning(message);
    }
  }

  private notifWarning(message: string){
    this.$notify({
      title: 'Warning',
      message: message,
      type: 'warning',
      duration: 3000
    });
  }

  private retrieveEVerificationLine(): void {
    if ( ! this.baseApiUrlEVerificationLine) {
      return;
    }

    this.processing = true;
    const paginationQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    };

    this.dynamicWindowService(this.baseApiUrlEVerificationLine)
      .retrieve({
        criteriaQuery: this.filterQuery,
      })
      .then(res => {
        this.gridData = res.data.map((item: any) => {
          item.totalAmount = item.totalLines + item.taxAmount;
          return item;
        });

        this.totalItems = Number(res.headers['x-total-count']);
        this.queryCount = this.totalItems;
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
      });
  }

  private retrieveGetCurrencies() {
    this.dynamicWindowService(this.baseApiUrlCurrency)
    .retrieve()
    .then(res => {
        let currencyList = res.data.map(item => {
            return{
                key: item.id,
                value: item.code
            };
        });
        this.currencyOptions = currencyList;

    });
  }

  addAllLines(data: any[]) {
    const lines = this.transformMatchPo(data);

    for (const line of lines) {
      const lineExist = (this.gridData.some((vLine: any) => vLine.id === line.id));

      if (!lineExist) {
        this.gridData.push(line);
      }
    }
    this.calculateLines();
    this.dialogMatchPoVisible = false;
  }

  addSelectedLines() {
    let totalLines = 0;
    let totalAmount = 0;
    let taxAmount = 0;
    const lines = this.transformMatchPo((<any>this.$refs.matchPo)?.selectedLines || []);

    for (const line of lines) {
      const lineExist = (this.gridData.some((vLine: any) => vLine.id === line.id));

      if (!lineExist) {
        this.gridData.push(line);
      }
    }
    this.calculateLines();
    this.dialogMatchPoVisible = false;
  }

  private calculateLines() {
    let totalLines = 0;
    let totalAmount = 0;
    let taxAmount = 0;

    for (const row of this.gridData) {
      totalLines += row.totalLines;
      totalAmount += row.totalAmount;
      taxAmount += row.taxAmount;
    }

    this.formUpdate.totalLines = totalLines;
    this.formUpdate.taxAmount = taxAmount;
    this.formUpdate.grandTotal = totalAmount;
  }

  onMatchPoOpen() {
    if (this.modeFilterMatchPo.mode === 1)
      (<any>this.$refs.matchPo)?.preloadData();
  }

  removeRow(row: any, index: number) {
    this.gridData.splice(index, 1);

    if (this.formUpdate.id) {
      this.removedLines.push(row);
    }

    this.formUpdate.totalLines -= row.totalLines;
    this.formUpdate.taxAmount -= row.taxAmount;
    this.formUpdate.grandTotal -= row.totalAmount;
  }

  submit() {
    this.formUpdate.picId = accountStore.userDetails.id;

    this.eVerification.form = this.formUpdate;
    this.eVerification.line = this.gridData;
    this.eVerification.remove = this.removedLines;

    console.log(this.eVerification);

    if (this.formUpdate.id != null) {
      this.dynamicWindowService(this.baseApiUrlEVerification + "/submit")
        .update(this.eVerification)
        .then(() => {

          this.$notify({
            title: 'Success',
            dangerouslyUseHTMLString: true,
            message: 'E-Verification form updated.',
            type: 'success'
          });

          this.closeEVerificationUpdate();

        }).catch(error => {
          this.$notify({
            title: 'Error',
            dangerouslyUseHTMLString: true,
            message: error,
            type: 'error',
            duration: 3000
          });
        }).finally(() => {
          this.fullscreenLoading = false;
        });

    } else {
      this.dynamicWindowService(this.baseApiUrlEVerification + "/submit")
        .create(this.eVerification)
        .then(() => {

          this.$notify({
            title: 'Success',
            dangerouslyUseHTMLString: true,
            message: 'E-Verification form submitted.',
            type: 'success'
          });

          this.closeEVerificationUpdate();

        }).catch(error => {
          this.$notify({
            title: 'Error',
            dangerouslyUseHTMLString: true,
            message: error,
            type: 'error',
            duration: 3000
          });
        }).finally(() => {
          this.fullscreenLoading = false;
        });

    }
  }

  private transformMatchPo(lines: any[]) {
    return lines.map(line => {
      const {
        cConversionRate,
        cDocType,
        cDocTypeMr,
        cTaxCategory,
        cVendor,
        dateAccount,
        lineNoPo,
        mMatchType,
        openAmount,
        openForeignAmount,
        openQty,
        orderSuffix,
        poDate,
        taxable,
        ...data
      } = line;

      line.conversionRate = data.cConversionRate;
      line.lineNo = data.lineNoMr;
      return line;
    });
  }

}