rm -r **/wr_*/
rm -r **/*_results.jtl

echo "... RUNNING MULTIPLE DELETION TEST ..."
cd test_multiple_deletion
./test_delete.sh
cd ..

echo "... RUNNING MULTITHREAD UPDATE TEST SUITE ..."
cd test_multithread_increase_price
./test_increase_price.sh
cd ..

echo "... RUNNING UNIQUE CONSTRAINT TEST ..."
cd test_unique_constraint
./test_unique.sh
cd ..

echo "... ALL TESTS WERE COMPLETED ..."
