
#Alsayer Setup Instruction 

This document helps you setup local environment for Alsayer Project. 

###Prerequisites 

*Download the OpenJDK Java 11 from **https://jdk.java.net/java-se-ri/11** and setup JAVA_HOME and PATH variables. 

*IntelliJ /Eclipse and VS Code Editor  

*Backend Development Requirement- Extract the sap commerce cloud zip downloaded from onedrive link shared: 

**https://aqtllc-my.sharepoint.com/:u:/r/personal/krishna_kumar_aspiredigital_com/Documents/Demo/CXCOM200500P_0-70004955.ZIP?csf=1&web=1&e=7ZTd6w** 

in your hybris installation directory. 

*Frontend Development Requirement- 

1.  node.js - 12.x version  

2.  Angular CLI- 10.x or later 

3.  yarn-1.15 or later 

 

###Installation Setup : 

1. Create a codebase directory for cloning the repository. Open the command prompt/terminal inside the direcectory and run the command :  

  **git clone https://github.com/aspiredigital-ae/Al-Sayer-DIP.git**  

It will ask for your github id and password, provide the details accordingly. 

2. Create your local branch for your custom changes: 

* For windows you can use Tortoise git or SourceTree to create a branch under origin/develop. 

* For Linux/Mac run command -   

  *To create and checkout from origin/develop to your branch 

   git checkout -b <name-of-your-branch> develop 

  *To create branch in the repo 

   git push -u origin <branch_name> 

  
2. Create symlinks for custom by running the following commands in cmd/terminal : 

  *For Windows : 

    *Symlink for custom folder from the Alsayer codebase 

     mklink /D {hybris_installation_dir}/hybris/bin/custom {alsayer_codebase_dir}/Al-Sayer-DIP/core-customize/custom  

  *For Linux/Mac : 

    *Symlink for custom folder from the Alsayer codebase 

     ln -s {alsayer_codebase_dir}/Al-Sayer-DIP/core-customize/custom {hybris_installation_dir}/hybris/bin/custom 


3. Copy and paste alsayer-receipe in your {hybris_installation_dir}/installer/recipes which is present your codebase.


4. Open a terminal or command prompt window inside the installer folder i.e., {hybris_installation_dir}/installer and set up the recipe using the following command: 

-  ./install.sh -r alsayer-receipe  

* If you are using **Windows**, change ./install.sh to install.bat. 


5.  Go to your platform directory to {hybris_installation_dir}/hybris/bin/platform  and run following commands to access the site - 

-For windows- 

  -setantenv.bat 

  -ant clean all  

  -ant initialize 

  -hybrisserver.bat debug  

 

For linux/mac- 

  -. ./setantenv.sh 

  -ant clean all  

  -ant initialize 

  -./hybrisserver.sh debug  

 
**You can access the storefront from link- https://localhost:9002/alsayerstorefront?site=electronics**


7. You can see the alsayerstore, the spartacus storefront present in repo/js-storefront/alsayerstore and can start server inside alsayerstore as: 

-yarn install 

-yarn start 

**And can access the spartacus site form link – https://locahost:4200**


8.  Create symlinks for localextensions.xml and local.properties by running following commands in cmd/terminal: 

-For Windows : 

  -Symlink for local.properties :  

   mklink /D {hybris_installation_dir}/hybris/config/local.properties {alsayer_codebase_dir}/Al-Sayer-DIP/core-customize/config/local.properties 

 

  -Symlink for localextensions.xml: 

   mklink /D {hybris_installation_dir}/hybris/config/localextensions.xml  {alsayer_codebase_dir}/Al-Sayer-DIP/core-customize/config/localextensions.xml 

 

-For Linux/Mac : 

  -Symlink for local.properties : 

   ln -s {alsayer_codebase_dir}/Al-Sayer-DIP/core-customize/config/local.properties {hybris_installation_dir}/hybris/config/local.properties 

 

  -Symlink for localextensions.xml: 

   ln -s {alsayer_codebase_dir}/Al-Sayer-DIP/core-customize/config/localextensions.xml {hybris_installation_dir}/hybris/config/localextensions.xml 

 

 
