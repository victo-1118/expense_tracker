# expense_tracker
expense tracker that will have frontend and backend. Will have ability to create cards (debit or credit) able to anlyze expenses based on date and categorys



3/6/2025
(Entities and objects are used interchangeably here.)
Did not start working on this project today but regardless I will start here. So first off i had to create a new 
repository since i messed up my gitignore so i had to delete my old repository. I also created a new repository
because i was having issues with gradle. Turns out that gradle was locking my files. This might have been caused
by changing the file names to much and overall just messing up the configuration of the file structure the initalzr
gave me.Then i had issues still which turned out to be not becuase of my properties conflicting with each other 
(one being normal and the other for test) but it was actually due SQL not creating a a new database and also due
to the fact that I havent been deleting the database even after changing the schema. 

After fixing that I had think about my data structure more. At first I had a many to many with an Expense entity
but the issue was that I wantedto have categories. That led to the creation of ExpenseTypeEntity. So i had a many
to many with that new class instead of Expense as i had a one to many with BaseCard and Expense. As for BaseCard
that came about since Credit and Debit card classes were similar so i wanted them to inherit from BaseCard
I chose to using single tabble to make it easy to make searches among these both different cards. Anyways
I had an issue with Expense as although I made the one to many relationship to make it easier to track transactions
I wanted multiple cards to also pay for multiple expenses. This would mean I would need a many to many here too but
I would need to force eligibility based on their type through the service layer. I also created a PaymentHistory
class to make it easeir for analytics. I created a one to many with the BaseCard class and Payment History and 
Expense class and Payment History. All of these changes will help me with the logic in Services.

As for my repository searches they are pretty standard except that most of them instead of using entitys to look 
for the corresponding object instead use the index to make it more efficient

Also recently i just added indexes to common variables where I will have to do searches like for amount between or
stuff like that.
