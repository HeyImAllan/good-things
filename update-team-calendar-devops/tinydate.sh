year=2021
month=$1
last=$(date -d "$year-$month-1 + 1 month - 1 day" +%d)
wday=$(date -d "$year-$month-1" +%w)
for (( day=1; day<=$last; day++ )); do
   if (( wday == 1 )); then
           printf "%4d-%02d-%02d\n" $year $month $day
   fi
   (( wday = (wday + 1) % 7 ))
done
