How to use the plugin
-------------------
Link on how to use local plugins: https://www.jetbrains.com/help/idea/managing-plugins.html
After, import it into your project under plugins using this configuration.

``` xml 
  <plugin>
        <groupId>sample.plugin</groupId>
        <artifactId>hello-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <sshHost>ip or hsot</sshHost>
            <sshUser>user</sshUser>
            <sshPassword>password</sshPassword>
            <sshCommand>ssh command</sshCommand>
            <uploadFile>file location(file included)</uploadFile>
            <uploadDestination>destination(without file)</uploadDestination>
        </configuration>
   </plugin>
 ```
