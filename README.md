# BIRT(Business Intelligence Reporting Tool) Report

## Install BIRT Designer in Eclipse

* Go to `Help` -> `Install New Software` -> Add `latest version URL`

	https://download.eclipse.org/birt/updates/release/latest
	


## Create package



## API docs

[Swagger docs](http://localhost:1991/swagger-ui/index.html)


## Test

Copy the `XML` from `/src/main/resources/designs/templates/first.xml` and paste it in the input box and try it to generate/download report

## Run Runtime engine

1. Download the file 

2. Give `Execution` permission to `./genReport.sh`

3. Execute the below command to remove `CR LF`

	sed -i -e 's/\r$//' genReport.sh
	
4. Then, run the following command for xml input
   ```
   ./genReport.sh -m runrender -f PDF -o /Users/thirumal/Downloads/report.pdf -p xmlFilePath=/Users/thirumal/git/birt-spring-boot/src/main/resources/designs/templates/first.xml /Users/thirumal/git/birt-spring-boot/src/main/resources/designs/templates/first.rptdesign
   ```
