
const API_URL = "api/shipments";

// Add a new shipment
document.getElementById("shipmentForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const shipment = {
            source: document.getElementById("source").value.trim(),
            destination: document.getElementById("destination").value.trim(),
            weight: Number(document.getElementById("weight").value),
            shipmentDate: document.getElementById("shipmentDate").value
        };

        const message = document.getElementById("message");

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(shipment)
            });

            if (!response.ok) {
                throw new Error("Unable to add shipment");
            }

            message.textContent = "Shipment added successfully!";
            message.style.color = "green";

            document.getElementById("shipmentForm").reset();

            loadShipments();

        } catch (error) {
            message.textContent = error.message;
            message.style.color = "red";
        }
    });


// Load all shipments
async function loadShipments() {

    const table = document.getElementById("shipmentTable");

    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error("Unable to load shipments");
        }

        const shipments = await response.json();

        table.innerHTML = "";

        if (shipments.length === 0) {
            table.innerHTML =
                "<tr><td colspan='7'>No shipments found</td></tr>";
            return;
        }

        shipments.forEach(shipment => {

            const row = document.createElement("tr");

            const values = [
                shipment.shipmentId,
                shipment.source,
                shipment.destination,
                shipment.weight,
                shipment.shipmentDate,
                shipment.status
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "";
                row.appendChild(cell);
            });

            const actionCell = document.createElement("td");
            const button = document.createElement("button");

            button.textContent = "Optimize Route";
            button.addEventListener("click", function() {
                optimizeRoute(shipment.shipmentId);
            });

            actionCell.appendChild(button);
            row.appendChild(actionCell);

            table.appendChild(row);
        });

    } catch (error) {
        table.innerHTML =
            "<tr><td colspan='7'>Error loading shipments</td></tr>";

        console.error(error);
    }
}


// Optimize route
async function optimizeRoute(shipmentId) {

    const result = document.getElementById("optimizationResult");

    result.textContent = "Calculating route costs...";

    try {
        const response = await fetch(
            "api/optimize?shipmentId=" + encodeURIComponent(shipmentId)
        );

        if (!response.ok) {
            throw new Error("Route optimization failed");
        }

        const routes = await response.json();

        if (routes.length === 0) {
            result.textContent =
                "No matching routes found for this shipment.";
            return;
        }

        result.innerHTML = "";

        const heading = document.createElement("h3");
        heading.textContent = "Available Routes";
        result.appendChild(heading);

        routes.forEach((route, index) => {

            const card = document.createElement("div");
            card.style.padding = "12px";
            card.style.margin = "10px 0";
            card.style.border = "1px solid #cbd5e1";
            card.style.borderRadius = "6px";

            const details = document.createElement("p");

            details.textContent =
                "Route " + route.routeId +
                " | " + route.source + " to " + route.destination +
                " | Distance: " + route.distance + " km" +
                " | Transport: " + route.transportMode +
                " | Estimated Cost: ₹" +
                Number(route.totalCost).toFixed(2);

            card.appendChild(details);

            if (index === 0) {
                const recommendation = document.createElement("strong");
                recommendation.textContent =
                    "Recommended: Lowest estimated cost";
                recommendation.style.color = "green";

                card.appendChild(recommendation);
            }

            result.appendChild(card);
        });

    } catch (error) {
        result.textContent = error.message;
        console.error(error);
    }
}


// Load shipments when page opens
document.addEventListener("DOMContentLoaded", loadShipments);
