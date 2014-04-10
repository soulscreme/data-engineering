Pre-Requesites
---------------------------
Play Framework v2.2 Installed and Configured

Starting the Application
---------------------------
On Linux/OS X:
	1. Launch a terminal/console.
	2. Change into the ls-data-eng-app directory.
	2. Type "play" and press enter.
	3. From the play prompt type "run" and press enter.
	4. Navigate to http://localhost:9000/ in your browser.
	5. After a small delay while the application compiles, you will receive a screen stating "Database 'default' needs evolution".  
	6. Click the "Apply this scipt now!" button.
	7. You will now be on the main screen of the application.
	
On Windows:
	1. Launch a command prompt.
	2. Change into the ls-data-eng-app directory.
	2. Type "play.bat" and press enter.
	3. From the play prompt type "run" and press enter.
	4. Navigate to http://localhost:9000/ in your browser.
	5. After a small delay while the application compiles, you will receive a screen stating "Database 'default' needs evolution".  
	6. Click the "Apply this scipt now!" button.
	7. You will now be on the main screen of the application.

Using the Application
---------------------------
The main screen contains 4 links at the top, these will switch the table being displayed below.
These tables allow for a view into how the data is stored in the database.

Below the 4 links and the table I display the current gross value represented by the database.

At the bottom of the main screen is a file chooser and button to upload a file.  Once the file is processed the
user will be brought back to the main screen with data now populated, including the gross value.

The four data types (database tables) I use are orders, merchants, customers, and items.

Orders are always considered new when processing a file.  A customer could buy the same deal from the same merchant
multiple times, therefore I did not apply any uniqueness here.

Merchants are considered unique based on name.  If a record comes in for a merchant with the same name (regardless of
case), then this record will be used instead of adding a new record.

Customer are also considered unique based on name.  If a record comes in for a customer with the same name (regardless
of case), then this record will be used instead of adding a new record.

Itmes are considered unique based on description, price, and the merchant that the item is for.  If a record comes
in for a merchant with the same description (regardless of case), price, and merchant then this record will be used
instead of adding a new record.

Shutdown/Clearing the Database
---------------------------
The application can be shutdown from the console b pressing CTRL+D and then typing "exit" and pressing enter.

If you have need to start over with a fresh database, this can be accomplished by doing the above and then restarting
the application.  This occurs because I used an in-memory H2 database to store the data.