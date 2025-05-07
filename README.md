<h1>OMNIX - ENTERPRISE MANAGEMENT PLATFORM</h1>

<h2>OVERVIEW</h2>
<p>Omnix is a microservice-based enterprise management platform designed to unify and streamline business processes such as CRM, supply chain, HR, and financial management. It offers a scalable, secure, and integrated solution to enhance operational efficiency and support data-driven decisions.</p>

<h2>PROJECT OBJECTIVE</h2>
<p>Developed Omnix to deliver a modular platform that integrates core business functions, ensuring flexibility, scalability, and seamless third-party integration for modern enterprises.</p>

<h2>KEY FEATURES</h2>
<ul>
    <li><b>CRM</b>: Manage customer data, interactions, and marketing campaigns.</li>
    <li><b>Supply Chain</b>: Optimize inventory, orders, and logistics with real-time tracking.</li>
    <li><b>HR Management</b>: Automate payroll, attendance, and training processes.</li>
    <li><b>Financial Management</b>: Handle accounting, invoicing, and financial reporting.</li>
    <li><b>Analytics</b>: Provide real-time insights and predictive reporting.</li>
    <li><b>Notifications</b>: Send email, SMS, or push notifications.</li>
    <li><b>Integrations</b>: Connect with tools like Salesforce, QuickBooks, and Google Maps.</li>
</ul>

<h2>TECHNOLOGIES</h2>
<ul>
    <li><b>Frameworks</b>: Spring Boot, Spring Cloud, Spring Security</li>
    <li><b>Databases</b>: MySQL, Neo4j, MongoDB, Redis, Elasticsearch</li>
    <li><b>Communication</b>: REST API, Kafka, WebSocket</li>
    <li><b>DevOps</b>: Docker</li>
    <li><b>Monitoring</b>: Prometheus, Grafana, ELK Stack, Zipkin</li>
    <li><b>Security</b>: OAuth2, JWT, Keycloak</li>
    <li><b>Tools</b>: Swagger, Maven, Git, Brevo</li>
</ul>

<h2>ARCHITECTURE</h2>
<p>Omnix uses a microservice architecture with independent services (e.g., Customer Service, Order Service) communicating via REST APIs, Kafka for events, and WebSocket for real-time updates. It is deployed using Docker and Kubernetes, with monitoring via Prometheus and Grafana.</p>

<h2>PREREQUISITES</h2>
<ul>
    <li>Java 17+</li>
    <li>Maven 3.8+</li>
    <li>Docker and Docker Compose</li>
    <li>Kubernetes (optional for production)</li>
    <li>MySQL, MongoDB, Redis, Kafka</li>
</ul>

<h2>INSTALLATION</h2>
<ol>
    <li>Clone the repository:
        <pre><code>git clone https://github.com/hieCoder/omnix.git
cd omnix</code></pre>
    </li>
    <li>Build the project:
        <pre><code>mvn clean install</code></pre>
    </li>
    <li>Run with Docker Compose:
        <pre><code>docker-compose up -d</code></pre>
    </li>
    <li>Access the API:
        <ul>
            <li>API Gateway: <code>http://localhost:8888</code></li>
        </ul>
    </li>
</ol>

<h2>CONTACT</h2>
<p>For questions, email <a href="mailto:hieuzzz601@gmail.com">hieuzzz601@gmail.com</a> or open an issue on GitHub.</p>
