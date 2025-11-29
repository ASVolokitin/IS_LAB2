rm -r wr_*/
rm -r *_results.jtl


jmeter -n -t test_unique_constraint_RR.jmx -l test_unique_constraint_RR_results.jtl -e -o wr_test_unique_constraint_RR_results/
jmeter -n -t test_unique_constraint_S.jmx -l test_unique_constraint_S_results.jtl -e -o wr_test_unique_constraint_S_results/
