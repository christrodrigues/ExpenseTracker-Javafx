# Expense Tracker JavaFX Project



An expense tracker using JavaFX is a powerful software application designed to assist users in managing their finances effectively. Built using JavaFX, a Java library for creating desktop applications, this Expense Tracker offers a user-friendly interface that simplifies expense tracking and management. Users can easily add, edit, and delete expenses, generate comprehensive reports and visually appealing graphs, import/export data, and set up alerts for upcoming bills and payments. The application employs a database to securely store and retrieve expense data, while JavaFX facilitates the creation of interactive and responsive user interfaces. The primary aim of this application is to empower users with insights into their financial situation, enabling them to make informed decisions regarding their spending and saving behaviors.

## System Design

The Expense Tracker system design is divided into several integral components, each contributing to an efficient and seamless user experience.

### User Interface

The user interface is thoughtfully designed to ensure simplicity and ease of use, with a modern and clean aesthetic. Key components of the user interface include:

- **Main Dashboard:** This central hub offers an overview of the user's financial status, showcasing a summary of total expenses categorized by type, recent transactions, savings, and the current balance.

- **Expense Input Form:** The expense input form empowers users to input detailed expense information such as the date, category, and amount. This feature serves as the foundation for accurate expense tracking.

- **Category Management:** Users can customize their expense categories, tailoring the tracker to their specific needs and preferences. This customizable feature enhances the accuracy and relevance of expense tracking.

### Reporting and Visualization

The reporting and visualization components provide users with comprehensive insights into their spending habits through visually engaging representations. Highlights include:

- **Graphical Reports:** Users can generate pie charts and graphs that visually display their spending patterns, aiding them in identifying areas for potential adjustments and savings.

### Data Storage and Management

The Expense Tracker system leverages a robust database for secure and efficient data storage and management. The database architecture comprises the following elements:

- **Expense Table:** This table stores detailed information about each expense, encompassing data such as the date, category, amount, and optional notes.

- **Category Table:** The category table stores essential category details, including the category name and a brief description.

- **User Table:** The user table houses user-specific information, including the user's name, email address, and login credentials.

## Getting Started

To run the Expense Tracker application on your local machine, follow these steps:

1. Clone the repository: `git clone https://github.com/christrodrigues/ExpenseTracker-Javafx.git`
2. Open the project in your preferred Java development environment (e.g., IntelliJ IDEA, Eclipse).
3. install wampserver and run it.
4. open `PhpMyAdmin` and create a new database with category, date and amount columns.
5. copy and paste the database link in ExpenseTracker.java file .
6. Build and run the application.

## Usage

1. Upon launching the application, you'll be greeted with the main dashboard, offering a snapshot of your financial status.
2. Use the expense input form to add new expenses, providing details such as date, category, and amount.
3. Explore the category management feature to customize expense categories.
4. Generate reports and visualizations to gain deeper insights into your spending habits.


