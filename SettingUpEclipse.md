# Prerequisites #

Before you can start working with the VisiGraph source code, you must first install the [Eclipse IDE for Java Developers](http://www.eclipse.org/downloads/) and [Subclipse plugin](http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA), which will allow you to sync to the most current version of the source code and submit changes.

_Note: although you may freely download and edit the VisiGraph source code on your local client, additional permissions and setup are required to commit changes to the main VisiGraph trunk.  To request write-access to the main trunk repository please email the project owner, [Cameron Behar](mailto:0x24a537r9@gmail.com)._

# Steps #

1. Start Eclipse and open the Java developer perspective, if not already open by default.


2. Within the package explorer panel on the left, right-click and select _New_ > _Other..._ to open the new project dialog.

![http://visigraph.googlecode.com/files/SettingUpEclipse1.png](http://visigraph.googlecode.com/files/SettingUpEclipse1.png)


3. Under the _SVN_ folder, select _Checkout Projects from SVN_ and click _Next_.

![http://visigraph.googlecode.com/files/SettingUpEclipse2.png](http://visigraph.googlecode.com/files/SettingUpEclipse2.png)


4. Select _Create a new repository location_ if not already selected and click _Next_.

![http://visigraph.googlecode.com/files/SettingUpEclipse3.png](http://visigraph.googlecode.com/files/SettingUpEclipse3.png)


5. Enter `https://visigraph.googlecode.com/svn` as the url and click _Next_.  If you do not use `https://` you will not be able to commit changes to source-control.

![http://visigraph.googlecode.com/files/SettingUpEclipse4.png](http://visigraph.googlecode.com/files/SettingUpEclipse4.png)


6. Select _VisiGraph_ under the _trunk_ folder and click _Finish_.

![http://visigraph.googlecode.com/files/SettingUpEclipse5.png](http://visigraph.googlecode.com/files/SettingUpEclipse5.png)


7. Assuming you have done everything correctly, Eclipse should begin checking out the VisiGraph project from SVN and building a new VisiGraph project (client) on your local machine.  If—once it is done—Eclipse does not look exactly the same as below or you have red x's next to any of the files in the VisiGraph project folder in the package explorer your client is probably not configured correctly.

![http://visigraph.googlecode.com/files/SettingUpEclipse6.png](http://visigraph.googlecode.com/files/SettingUpEclipse6.png)


_Should you have any problems configuring a new VisiGraph client using the directions above feel free to email the project owner, [Cameron Behar](mailto:0x24a537r9@gmail.com)._