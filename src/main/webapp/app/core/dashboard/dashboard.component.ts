import DynamicWindowService from '@/core/application-dictionary/components/DynamicWindow/dynamic-window.service';
import WatchList from "@/core/dashboard/components/watch-list.vue";
import { AccountStoreModule as accountStore } from "@/shared/config/store/account-store";
import { IPaDashboardPreference } from '@/shared/model/pa-dashboard-preference.model';
import { Component, Inject, Vue } from 'vue-property-decorator';
import DashboardService from './dashboard.service';
import { debounce } from 'lodash';

@Component({
  components: {
    WatchList
  }
})
export default class DashBoard extends Vue {

  @Inject('dynamicWindowService')
  protected commonService: (baseApiUrl: string) => DynamicWindowService;

  private dashboardService: DashboardService;
  private debouncedRefresh: Function;

  dashboardItems: IPaDashboardPreference[] = [];

  get dashboards() {
    const list = [];
    for (const [key] of accountStore.grantedDashboards) {
      const keys = key.split('_');

      list.push({
        key,
        id: keys[0],
        name: keys[1]
      });
    }
    return list;
  }

  created() {
    this.dashboardService = new DashboardService(this);
    this.debouncedRefresh = debounce(this.refresh, 5000);

    if (this.dashboards.length) {
      this.switchDashboard(this.dashboards[0].key);
    }
  }

  mounted() {
    this.dashboardService.connect();
    this.dashboardService.subscribe();
    this.dashboardService.receive().subscribe(() => {
      this.debouncedRefresh();
    });
  }

  beforeDestroy() {
    this.dashboardService.unsubscribe();
  }

  switchDashboard(key: string) {
    if (accountStore.grantedDashboards.has(key)) {
      const items = [...accountStore.grantedDashboards.get(key)];

      this.commonService('/api/pa-dashboard-preferences')
      .retrieve({
        criteriaQuery: [
          'active.equals=true',
          `adUserId.equals=${accountStore.userDetails.id}`,
          [...items.map(id => `paDashboardItemId.in=${id}`)]
        ],
        paginationQuery: {
          page: 0,
          size: 50,
          sort: ['id']
        }
      })
      .then(res => {
        this.dashboardItems = res.data;
      })
      .catch(err => {
        console.log('Dashboard error', err);
        this.$message({
          message: 'Failed to load dashboard items.',
          type: 'error'
        });
      });
    }
  }

  public refresh() {
    const widgets: any[] = <any[]>this.$refs.widget;
    widgets?.forEach((widget, index) => {
      console.log('Attempt to refresh widget#%d', index);
      widget.refresh();
    });
  }
}