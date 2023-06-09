CREATE DATABASE CentralDB;
GO

CREATE DATABASE FirstBranchDB;
GO

CREATE DATABASE SecondBranchDB;
GO

CREATE DATABASE ThirdBranchDB;
GO

USE CentralDB;
CREATE LOGIN [central_login] WITH PASSWORD = 'Central_password1';

USE FirstBranchDB;
CREATE LOGIN [first_branch_login] WITH PASSWORD = 'First_branch_password1';

USE SecondBranchDB;
CREATE LOGIN [second_branch_login] WITH PASSWORD = 'Second_branch_password1';

USE ThirdBranchDB;
CREATE LOGIN [third_branch_login] WITH PASSWORD = 'Third_branch_password1';

USE CentralDB;
CREATE USER [central_user] FOR LOGIN [central_login];
ALTER ROLE db_owner ADD MEMBER [central_user];

USE FirstBranchDB;
CREATE USER [first_branch_user] FOR LOGIN [first_branch_login];
ALTER ROLE db_owner ADD MEMBER [first_branch_user];

USE SecondBranchDB;
CREATE USER [second_branch_user] FOR LOGIN [second_branch_login];
ALTER ROLE db_owner ADD MEMBER [second_branch_user];

USE ThirdBranchDB;
CREATE USER [third_branch_user] FOR LOGIN [third_branch_login];
ALTER ROLE db_owner ADD MEMBER [third_branch_user];