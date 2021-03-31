bearer_token="mytoken123"
content_type="application/json"
accept="application/json;api-version=3.2-preview.1;excludeUrls=true;enumsAsNumbers=true;msDateFormat=true;noArrayWrap=true"
origin="https://dev.azure.com"
url="https://extmgmt.dev.azure.com/INGCDaaS/_apis/ExtensionManagement/InstalledExtensions/ms-devlabs/team-calendar/Data/Scopes/Default/Current/Collections/d9c3985c-af1a-49da-8bcd-e0da228a83e5.3.2021/Documents"
method="POST"
XML1="{\"category\":\"Regular day off\",\"description\":\"\",\"endDate\":\""
XML2="T00:00:00.000Z\",\"startDate\":\""
XML3="T00:00:00.000Z\",\"title\":\"Allan: ADV\"}"
user_agent="insomnia/2021.1.1"
for month in 6 7 8 9 10 11 12; do

for mydate in $(./tinydate.sh $month); do
	curl -d "$XML1$mydate$XML2$mydate$XML3" -H "accept: $accept" -H "origin: $origin" -H "Content-Type: $content_type" -H "Authorization: Bearer $bearer_token" -H "user-agent: $user_agent" -X $method $url
done
done
