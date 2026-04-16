# BookstoreApp
This application is a comprehensive mobile solution digitalizing bookstore operations through a dual-interface system supporting administrative inventory management and customer purchasing. Built using Java and SQLite, it demonstrates modern Android development practices while maintaining user-friendly interfaces.

The application focused mainly on the following features:
• Digital inventory management for administrators
• Customer accessibility for browsing and purchasing
• Real-time stock management with automatic transaction updates
• Role-based access control with secure authentication
• Comprehensive transaction history tracking
• Material Design principles
• Showcase complex database relationships
• Secure session management
• Scalable architecture for future enhancements

The authentication system validates credentials against SQLite database records, maintains sessions using SharedPreferences, and provides role-based navigation directing users to appropriate dashboards. Administrator features include a comprehensive dashboard displaying inventory statistics and personalized welcomes, plus inventory management enabling book catalog viewing, new book additions with complete metadata, and real-time updates through reactive data binding.

Customer features provide card-based book browsing with title, author, price, and stock information, utilizing RecyclerView optimization for smooth scrolling. The purchase process offers detailed book views, quantity selection with stock validation, instant confirmation, and automatic inventory updates through atomic transactions. Purchase history maintains chronological transaction records with comprehensive details, accessible through bottom navigation following Material Design guidelines.

Security measures include password storage (requiring production hashing with bcrypt/PBKDF2), SharedPreferences session management in private mode, application-level role-based access control, multi-level input validation, parameterized queries preventing SQL injection, transaction integrity through ACID compliance, and proper exception handling following OWASP guidelines.
