rm -r wr_*/ *_results.jtl

jmeter -n -t test_mutithread_increase_price.jmx -l test_mutithread_increase_price_results.jtl -e -o wr_test_mutithread_increase_price_results/
jmeter -n -t test_mutithread_increase_price_retry.jmx -l test_mutithread_increase_price_retry_results.jtl -e -o wr_test_mutithread_increase_price_retry_results/
jmeter -n -t test_mutithread_increase_price_retry_extra.jmx -l test_mutithread_increase_price_retry_extra_results.jtl -e -o wr_test_mutithread_increase_price_retry_extra_results/
