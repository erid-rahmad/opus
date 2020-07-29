import Vue from 'vue'
import Component from 'vue-class-component'
import BusinessCategories from './components/business-categories.vue'
import CompanyProfile from './components/company-profile.vue'
import LoginDetails from './components/login-details.vue'
import SupportingDocuments from './components/supporting-documents.vue'
import PersonInCharge from './components/person-in-charge.vue'
import PaymentInformation from './components/payment-information.vue'
import TaxInformation from './components/tax-information.vue'
import SummaryRegistration from './components/summary-registration.vue'
import { Inject } from 'vue-property-decorator'
import RegisterService from './register.service'
import { LOGIN_ALREADY_USED_TYPE, EMAIL_ALREADY_USED_TYPE } from '@/constants'

@Component({
  components: {
    LoginDetails,
    CompanyProfile,
    BusinessCategories,
    SupportingDocuments,
    PersonInCharge,
    PaymentInformation,
    TaxInformation,
    SummaryRegistration
  }
})

export default class StepsForm extends Vue {

  @Inject('registerService') private registerService: () => RegisterService;
  
  submitAgree: boolean = false;
  doNotMatch = '';
  error = '';
  errorEmailExists = '';
  errorUserExists = '';
  success = false;

  loading = false;
  active = 0;
  eventBus = new Vue();
  registration = {
    loginDetails: {
      login: 'budi',
      email: 'budi@gmail.com',
      password: 'budi',
      passwordConfirmation: 'budi'
    },
    companyProfile: {
      name: 'PT. Terbaik',
      npwp: '928291918290001',
      npwpName: 'John',
      npwpAddress: 'Jl. Pemuda',
      npwpCountry: 'INDONESIA',
      npwpRegion: 'DKI JAKARTA',
      npwpCity: 'JAKARTA SELATAN',
      npwpPostalCode: '',
      type: 'C',
      branch: false,
      address: 'Jl. Sudirman',
      country: 'INDONESIA',
      region: 'DKI JAKARTA',
      city: 'JAKARTA SELATAN',
      postalCode: '',
      phone: '303030',
      fax: '',
      email: 'cs@terbaik.com',
      website: ''
    },
    businessCategories: {
      values: []
    },
    mainDocuments: [],
    additionalDocuments: [],
    contacts: [],
    functionaries: [],
    payments: [],
    taxInformations: {
      efaktur: '',
      pkp: '',
    },
    taxRates: []
  }
  
  mounted() {
    this.eventBus.$on('step-validated', this.proceedNext);
  }

  back() {
    this.$router.go(-1);
  }

  previous() {
    if ((this.active < 3)||(this.active > 3)) {
      --this.active;
    }else if(this.active === 3){
      --this.active;
      //this.registration.mainDocuments = [];
      //this.registration.additionalDocuments = [];
      
    }
  }

  next() {
    // Trigger the validation of the current form.
    this.eventBus.$emit('validate-form', this.active);
  }
  
  proceedNext(validationState) {
    if (validationState.passed && this.active <= 7) {
      ++this.active;
    }
  }

  showAt(index) {
    return index !== this.active ? 'hide' : '';
  }

  submit(){
    
      if(this.submitAgree){
        console.log(this.registration);
        
        /*
          this.doNotMatch = null;
          this.error = null;
          //this.errorUserExists = null;
          //this.errorEmailExists = null;
          //this.registerAccount.langKey = translationStore.language;
          this.registerService()
          .processRegistration(this.registration.loginDetails)
          .then(() => {
            this.success = true;
            this.$notify({
              title: 'Success',
              dangerouslyUseHTMLString: true,
              message: '<strong>Registration saved!</strong> Please check your email for confirmation.',
              type: 'success',
              duration: 3000
            });
          })
          .catch(error => {
            this.success = null;
            if (error.response.status === 400 && error.response.data.type === LOGIN_ALREADY_USED_TYPE) {
              this.error = '<strong>Login name already registered!</strong> Please choose another one.';
            } else if (error.response.status === 400 && error.response.data.type === EMAIL_ALREADY_USED_TYPE) {
              this.error = '<strong>Email is already in use!</strong> Please choose another one.';
            } else {
              this.error = '<strong>Registration failed!</strong> Please try again later.';
            }

            this.$notify({
              title: 'Error',
              dangerouslyUseHTMLString: true,
              message: this.error,
              type: 'error',
              duration: 3000
            });
          });
        */
        
      }else{
        this.$notify({
          title: 'Warning',
          message: "please check agreement",
          type: 'warning',
          duration: 3000
        });
      }
    
    this.loading = false;
    
  }
  
}